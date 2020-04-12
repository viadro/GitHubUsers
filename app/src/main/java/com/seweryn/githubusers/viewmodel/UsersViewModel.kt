package com.seweryn.githubusers.viewmodel

import androidx.lifecycle.MutableLiveData
import com.seweryn.githubusers.data.UsersRepository
import com.seweryn.githubusers.data.model.User
import com.seweryn.githubusers.utils.SchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(schedulerProvider) {

    private var allFetchedUsers: List<User> = listOf()

    val users: MutableLiveData<List<User>> = MutableLiveData()
    val progress: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Boolean> = MutableLiveData()
    val loadMoreUsersAllowed: MutableLiveData<Boolean> = MutableLiveData()

    init {
        users.value = mutableListOf()
        loadMoreUsersAllowed.value = true
        loadUsers(usersRepository.getUsers())
    }

    fun loadMoreUsers() {
        if(progress.value == true) return
        users.value?.last()?.id?.let {
            loadUsers(usersRepository.paginateUsers(it))
        }
    }

    fun searchPhrase(phrase: String) {
        error.value = false
        if(phrase.isEmpty()) {
            users.value = allFetchedUsers
            loadMoreUsersAllowed.value = true
            return
        }
        users.value?.let {
            loadMoreUsersAllowed.value = false
            users.value = allFetchedUsers.filter { user ->
                    user.login.startsWith(phrase, true) || user.repositories.any { repository ->
                        repository.name.startsWith(phrase, true)
                    }
                }
        }
    }

    private fun loadUsers(loadUsersCommand: Single<List<User>>) {
        clearSubscriptions()
        progress.value = true
        error.value = false
        load(
            command = loadUsersCommand,
            onSuccess = { result ->
                progress.value = false
                users.value?.let {
                    if(result.isEmpty()) return@let
                    users.value = it.union(result).toList()
                    allFetchedUsers = users.value!!
                }
            },
            onError = {
                progress.value = false
                error.value = true
            }
        )

    }
}