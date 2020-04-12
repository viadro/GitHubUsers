package com.seweryn.githubusers.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.seweryn.githubusers.data.UsersRepository
import com.seweryn.githubusers.data.model.Repository
import com.seweryn.githubusers.data.model.User
import com.seweryn.githubusers.utils.SchedulerProvider
import com.seweryn.githubusers.utils.WithMockito
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class UsersViewModelTest : WithMockito {

    private lateinit var systemUnderTest: UsersViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var schedulersProvider: SchedulerProvider

    @Mock
    lateinit var usersRepository: UsersRepository

    @Mock
    lateinit var progressListener: (Boolean) -> Unit

    @Before
    fun setUp() {
        simulateUsersCanBeLoaded()
        mockSchedulers()
        systemUnderTest = UsersViewModel(usersRepository, schedulersProvider)
        mockLiveData()
    }

    @Test
    fun `should load events on initialisation`() {
        Mockito.verify(usersRepository).getUsers()
    }

    @Test
    fun `should allow for loading more users on initialisation`() {
        assert(systemUnderTest.loadMoreUsersAllowed.value == true)
    }

    @Test
    fun `should update data when loaded users`() {
        assertNotNull(systemUnderTest.users.value)
        assertEquals(2, systemUnderTest.users.value?.size)
    }

    @Test
    fun `should load more events when requested`() {
        simulateMoreUsersCanBeLoaded()
        systemUnderTest.loadMoreUsers()
        Mockito.verify(usersRepository).paginateUsers(ArgumentMatchers.anyInt())

    }

    @Test
    fun `should update data when loaded more users`() {
        simulateMoreUsersCanBeLoaded()
        systemUnderTest.loadMoreUsers()
        assertEquals(4, systemUnderTest.users.value?.size)
    }

    @Test
    fun `should filter users which login starts with search phrase`() {
        systemUnderTest.searchPhrase("na")
        assertEquals(1, systemUnderTest.users.value?.size)
    }

    @Test
    fun `should filter users which login repository name with search phrase`() {
        systemUnderTest.searchPhrase("rep")
        assertEquals(1, systemUnderTest.users.value?.size)
    }

    @Test
    fun `should filter out users which login or repository name does not start with search phrase`() {
        systemUnderTest.searchPhrase("gi")
        assertEquals(0, systemUnderTest.users.value?.size)
    }

    @Test
    fun `should not allow to load more users when searching phrase`() {
        systemUnderTest.searchPhrase("a")
        assert(systemUnderTest.loadMoreUsersAllowed.value == false)
    }

    @Test
    fun `should allow to load more users again when searching phrase is cleared`() {
        systemUnderTest.searchPhrase("a")
        systemUnderTest.searchPhrase("")
        assert(systemUnderTest.loadMoreUsersAllowed.value == true)
    }

    @Test
    fun `should not attempt to load more users when loading is already in progress`() {
        systemUnderTest.progress.value = true
        systemUnderTest.loadMoreUsers()
        verify(usersRepository, never()).paginateUsers(ArgumentMatchers.anyInt())
    }

    @Test
    fun `should show error on fail`() {
        simulateMoreUsersCanNotBeLoaded()
        systemUnderTest.loadMoreUsers()
        assert(systemUnderTest.error.value == true)
    }

    @Test
    fun `should hide error on retry`() {
        simulateMoreUsersCanNotBeLoaded()
        systemUnderTest.loadMoreUsers()
        simulateMoreUsersCanBeLoaded()
        systemUnderTest.loadMoreUsers()
        assert(systemUnderTest.error.value == false)
    }

    private fun mockLiveData() {
        systemUnderTest.loadMoreUsersAllowed.observeForever { }
        systemUnderTest.users.observeForever {  }
        systemUnderTest.progress.observeForever{ progressListener.invoke(it) }
    }

    private fun mockSchedulers() {
        `when`(schedulersProvider.ioScheduler()).thenReturn(Schedulers.trampoline())
        `when`(schedulersProvider.uiScheduler()).thenReturn(Schedulers.trampoline())
    }

    private fun simulateUsersCanBeLoaded() {
        `when`(usersRepository.getUsers()).thenReturn(Single.just(createUsers(1, 2)))
    }

    private fun simulateMoreUsersCanBeLoaded() {
        `when`(usersRepository.paginateUsers(ArgumentMatchers.anyInt())).thenReturn(Single.just(createUsers(3, 4)))
    }

    private fun simulateMoreUsersCanNotBeLoaded() {
        `when`(usersRepository.paginateUsers(ArgumentMatchers.anyInt())).thenReturn(Single.error(Exception()))
    }

    private fun createUsers(id: Int, id2: Int) = listOf(
        User(
            id = id,
            login = "login",
            avatarUrl = "url",
            repositories = listOf(Repository("repo"))
        ),
        User(
            id = id2,
            login = "name",
            avatarUrl = "url",
            repositories = listOf(Repository("test"))
        )
    )
}