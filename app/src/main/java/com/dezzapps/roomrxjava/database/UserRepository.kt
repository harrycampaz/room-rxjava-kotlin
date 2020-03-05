package com.dezzapps.roomrxjava.database

import com.dezzapps.roomrxjava.model.User
import io.reactivex.Flowable

class UserRepository (private val mLocationDataSource: IUserDataSource): IUserDataSource {

    override val allusers: Flowable<List<User>>
        get() = mLocationDataSource.allusers

    override fun getUserById(userId: Int): Flowable<User> {

       return  mLocationDataSource.getUserById(userId)

    }

    override fun insertUser(vararg users: User) {
       mLocationDataSource.insertUser(*users)
    }

    override fun updateUser(vararg users: User) {
        mLocationDataSource.insertUser(*users)
    }

    override fun deleteUser(user: User) {
        mLocationDataSource.deleteUser(user)
    }

    override fun deleteAllUser() {

        mLocationDataSource.deleteAllUser()

    }

    companion object {
        private var mInstance: UserRepository ?= null

        fun getInstance(mLocationDataSource: IUserDataSource) : UserRepository{
            if(mInstance == null) {
                mInstance = UserRepository(mLocationDataSource)
            }
            return mInstance!!
        }
    }


}