package com.dezzapps.roomrxjava.database

import com.dezzapps.roomrxjava.model.User
import io.reactivex.Flowable

interface IUserDataSource {

    val allusers: Flowable<List<User>>

    fun getUserById(userId: Int): Flowable<User>
    fun insertUser(vararg users:User)
    fun updateUser(vararg users:User)
    fun deleteUser(user:User)
    fun deleteAllUser()

}