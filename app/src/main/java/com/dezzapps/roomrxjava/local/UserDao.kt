package com.dezzapps.roomrxjava.local

import androidx.room.Query
import com.dezzapps.roomrxjava.model.User
import io.reactivex.Flowable

interface UserDao {

    @get: Query("select * from users")
    val allUsers: Flowable<List<User>>

    @get:Query("select * from users where id= :userId")
    fun getUserById(userId: Int): Flowable<user>
}