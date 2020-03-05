package com.dezzapps.roomrxjava

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.dezzapps.roomrxjava.database.UserRepository
import com.dezzapps.roomrxjava.local.UserDataBase
import com.dezzapps.roomrxjava.local.UserDataSource
import com.dezzapps.roomrxjava.model.User
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    lateinit var adapter: ArrayAdapter<*>
    var userList: MutableList<User> = ArrayList()

    private var compositeDisposable: CompositeDisposable?= null
    private var userRepository: UserRepository ?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init

        compositeDisposable = CompositeDisposable()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userList)

        registerForContextMenu(lst_users)
        lst_users!!.adapter = adapter

        val userDatabase = UserDataBase.getInstance(this)

        userRepository = UserRepository.getInstance(UserDataSource.getInstance(userDatabase.userDao()))

        loadData()

        fab_add.setOnClickListener {
            val disposable = Observable.create(ObservableOnSubscribe<Any> {


                    e->
                val user = User()
                user.name = "my User"
                user.email = UUID.randomUUID().toString()+"@mail.com"
                userRepository!!.insertUser(user)

                e.onComplete()
            })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(io.reactivex.functions.Consumer{},
                    io.reactivex.functions.Consumer { throwable-> Toast.makeText(this, ""+ throwable.message, Toast.LENGTH_SHORT).show()
                    }, Action { loadData()})

            compositeDisposable!!.add(disposable)
        }
    }

    private fun loadData() {
        val disposable = userRepository!!.allusers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({users -> onGetAllUserSucccess(users)}){
                    throwable -> Toast.makeText(this, ""+ throwable.message, Toast.LENGTH_SHORT ).show()
                }

        compositeDisposable!!.add(disposable)
    }

    private fun onGetAllUserSucccess(users: List<User>) {

        userList.clear()
        userList.addAll(users)
        adapter.notifyDataSetChanged()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.deleteAction -> deleteAllUsers()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUsers() {

        val disposable = Observable.create(ObservableOnSubscribe<Any> { e-> userRepository!!.deleteAllUser()

            e.onComplete()
        })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(io.reactivex.functions.Consumer{},
                io.reactivex.functions.Consumer {
                        throwable-> Toast.makeText(this, ""+ throwable.message, Toast.LENGTH_SHORT).show()
                }, Action { loadData()})

        compositeDisposable!!.add(disposable)

    }

    override fun onCreateContextMenu( menu: ContextMenu,  v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {

        super.onCreateContextMenu(menu, v, menuInfo)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        menu.setHeaderTitle("Select ")

        menu.add(Menu.NONE, 0, Menu.NONE, "Update")
        menu.add(Menu.NONE, 0, Menu.NONE, "Delete")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        val user = userList[info.position]

        when(item.itemId){

            // Edit
            0 -> {
                val editName = EditText(this)
                editName.setText(user.name)
                editName.hint = "Enter your name"

                // Dialog

                AlertDialog.Builder(this)
                    .setTitle("Edit")
                    .setMessage("Edit Username")
                    .setView(editName)
                    .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->

                        if (TextUtils.isEmpty(editName.text.toString())){
                            return@OnClickListener
                        }else {
                            user.name = editName.text.toString()
                            updateUser(user)
                        }
                    }).setNegativeButton(android.R.string.cancel){
                        dialog, which -> dialog.dismiss()
                    }.create()
                    .show()

            }

            //Delete
            1 -> {
                AlertDialog.Builder(this)
                    .setMessage("Do  you wanna to delete: " + user.name)
                    .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener {
                        dialog, which -> deleteUser(user)
                    }).setNegativeButton(android.R.string.cancel){
                            dialog, which -> dialog.dismiss()
                    }.create()
                    .show()
            }

        }

        return  true
    }

    private fun deleteUser(user: User) {

        val disposable = Observable.create(ObservableOnSubscribe<Any> {
                e-> userRepository!!.deleteUser(user)

            e.onComplete()
        })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(io.reactivex.functions.Consumer{},
                io.reactivex.functions.Consumer {
                        throwable-> Toast.makeText(this, ""+ throwable.message, Toast.LENGTH_SHORT).show()
                }, Action { loadData()})

        compositeDisposable!!.add(disposable)
    }

    private fun updateUser(user: User) {
        val disposable = Observable.create(ObservableOnSubscribe<Any> {
                e-> userRepository!!.updateUser(user)

            e.onComplete()
        })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(io.reactivex.functions.Consumer{},
                io.reactivex.functions.Consumer {
                        throwable-> Toast.makeText(this, ""+ throwable.message, Toast.LENGTH_SHORT).show()
                }, Action { loadData()})

        compositeDisposable!!.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable!!.clear()
        super.onDestroy()
    }
}
