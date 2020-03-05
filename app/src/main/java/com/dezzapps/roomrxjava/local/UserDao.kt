package com.dezzapps.roomrxjava.local

import androidx.room.*
import com.dezzapps.roomrxjava.model.User
import io.reactivex.Flowable

@Dao
interface UserDao {

    @get: Query("select * from users")
    val allUsers: Flowable<List<User>>

    @Query("select * from users where id= :userId")
    fun getUserById(userId: Int): Flowable<User>

    @Insert
    fun insertUser(vararg users: User)

    @Update
    fun  updateUser(vararg  users: User)

    @Delete
    fun deleteUser(user: User)

    @Query("delete from users")
    fun deleteAllUser()


}