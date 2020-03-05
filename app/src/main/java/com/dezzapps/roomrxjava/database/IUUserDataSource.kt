package com.dezzapps.roomrxjava.database

import com.dezzapps.roomrxjava.model.User
import io.reactivex.Flowable

interface IUUserDataSource {

    val alluser: Flowable<List<User>>

    fun getUserById(userId: Int): Flowable<User>

}