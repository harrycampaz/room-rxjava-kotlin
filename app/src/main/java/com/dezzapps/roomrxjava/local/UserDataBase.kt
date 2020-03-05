package com.dezzapps.roomrxjava.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dezzapps.roomrxjava.local.UserDataBase.Companion.DATABASE_VERSION
import com.dezzapps.roomrxjava.model.User

@Database(entities = arrayOf(User::class), version = DATABASE_VERSION)
abstract class UserDataBase: RoomDatabase(){
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_VERSION = 1
        val DATABASE_NAME =  "DEZZAPPS-database"

        private var mInstance: UserDataBase?= null

        fun getInstance(context: Context): UserDataBase{
            if(mInstance == null)
                mInstance = Room.databaseBuilder(context, UserDataBase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            return mInstance!!
        }
    }
}