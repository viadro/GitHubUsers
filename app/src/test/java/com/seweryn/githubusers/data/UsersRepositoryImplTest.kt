package com.seweryn.githubusers.data

import com.seweryn.githubusers.data.local.Database
import com.seweryn.githubusers.data.local.UsersDao
import com.seweryn.githubusers.data.model.Repository
import com.seweryn.githubusers.data.model.User
import com.seweryn.githubusers.data.remote.GitHubApi
import com.seweryn.githubusers.utils.WithMockito
import com.seweryn.githubusers.utils.network.ConnectionManager
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class UsersRepositoryImplTest : WithMockito {

    private lateinit var systemUnderTest: UsersRepositoryImpl

    @Mock
    lateinit var api: GitHubApi

    @Mock
    lateinit var database: Database

    @Mock
    lateinit var usersDao: UsersDao

    @Mock
    lateinit var connectionManager: ConnectionManager

    @Before
    fun setUp() {
        systemUnderTest = UsersRepositoryImpl(api, database, connectionManager)
        mockDatabase()
    }

    @Test
    fun `should combine user with his repository`() {
        simulateInternetAccess()
        simulateRepositoriesCanBeFetched()
        simulateUsersCanBeFetched()
        val result = systemUnderTest.getUsers().test().values().first()
        assertEquals(1, result.size)
        assert(!result[0].repositories.isNullOrEmpty())
    }

    @Test
    fun `should take first three user repositories`() {
        simulateInternetAccess()
        simulateRepositoriesCanBeFetched()
        simulateUsersCanBeFetched()
        val result = systemUnderTest.getUsers().test().values().first()
        assertEquals(3, result[0].repositories.size)
    }

    @Test
    fun `should save fetched users to database`() {
        simulateInternetAccess()
        simulateRepositoriesCanBeFetched()
        simulateUsersCanBeFetched()
        systemUnderTest.getUsers().test()
        verify(usersDao).insertUsers(any())
    }

    @Test
    fun `should load users from database when no internet connection`() {
        simulateNoInternetAccess()
        simulateUsersCanBeLoadedFromDatabase()
        systemUnderTest.getUsers().test()
        verify(usersDao).queryUsers()
    }

    @Test
    fun `should return empty list when requested pagination and no internet connection`() {
        simulateNoInternetAccess()
        val result = systemUnderTest.paginateUsers(2).test().values().first()
        assertEquals(0, result.size)
    }

    private fun simulateInternetAccess(){
        `when`(connectionManager.isConnected()).thenReturn(true)
    }

    private fun simulateNoInternetAccess(){
        `when`(connectionManager.isConnected()).thenReturn(false)
    }

    private fun simulateUsersCanBeFetched() {
        `when`(api.getUsers(any())).thenReturn(Single.just(createUsers()))
    }

    private fun simulateRepositoriesCanBeFetched() {
        `when`(api.getUserRepositories(ArgumentMatchers.anyString())).thenReturn(Single.just(createRepositories()))
    }

    private fun simulateUsersCanBeLoadedFromDatabase() {
        `when`(usersDao.queryUsers()).thenReturn(Single.just(createUsers()))
    }

    private fun mockDatabase() {
        `when`(database.usersDao()).thenReturn(usersDao)
    }

    private fun createUsers() = listOf(
        User(
            id = 1,
            login = "login",
            avatarUrl = "url",
            repositories = listOf()
        )
    )

    private fun createRepositories() = listOf(
        Repository(name = "name"),
        Repository(name = "name"),
        Repository(name = "name"),
        Repository(name = "name"),
        Repository(name = "name"),
        Repository(name = "name")
    )
}