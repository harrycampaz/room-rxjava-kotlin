package com.dezzapps.roomrxjava.local

import com.dezzapps.roomrxjava.database.IUserDataSource
import com.dezzapps.roomrxjava.database.UserRepository
import com.dezzapps.roomrxjava.model.User
import io.reactivex.Flowable

class UserDataSource(private val userDao: UserDao): IUserDataSource{
    override val allusers: Flowable<List<User>>
        get() = userDao.allUsers
    override fun getUserById(userId: Int): Flowable<User> {
        return userDao.getUserById(userId)
    }


    override fun insertUser(vararg users: User) {

        userDao.insertUser(*users)
    }

    override fun updateUser(vararg users: User) {

        userDao.updateUser(*users)
    }

    override fun deleteUser(user: User) {

        userDao.deleteUser(user)
    }

    override fun deleteAllUser() {

        userDao.deleteAllUser()
    }


    companion object {
        private var mInstance: UserDataSource?= null

        fun getInstance(userDao: UserDao) : UserDataSource {
            if(mInstance == null)
                mInstance = UserDataSource(userDao)
            return mInstance!!
        }
    }

}