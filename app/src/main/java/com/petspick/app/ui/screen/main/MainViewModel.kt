package com.petspick.app.ui.screen.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.petspick.app.R
import com.petspick.app.api.FirebaseRepository
import com.petspick.app.api.Message
import com.petspick.app.api.UserInfo
import com.petspick.app.data.storage.models.Announcement
import com.petspick.app.data.storage.repository.BDRepository
import com.petspick.app.utils.Const
import com.petspick.app.utils.ToastHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val bdRepository: BDRepository,
): ViewModel() {

    companion object {
        var user: FirebaseUser? = null
        var userInfo = MutableStateFlow(UserInfo())
        var openChat = MutableStateFlow(Announcement())

        var lastWindow = MutableStateFlow(Window.HOME)

        var myAnnouncementList = mutableStateOf(listOf<Announcement>())
        var announcementList = mutableStateOf(listOf<Announcement>())
        var usersList = mutableStateOf(listOf<UserInfo>())
        var favoritesList = mutableStateOf(listOf<Announcement>())
        var messageList = mutableStateOf(listOf<Message>())

        fun setLastWindow(window: Window) {
            lastWindow.value = window
        }
    }

    fun login( activity: Activity, login: String, password: String, callback: (FirebaseUser?) -> Unit) {
        if (login.isEmpty() || password.isEmpty()){
            ToastHelper().show(activity, activity.getString(R.string.toastEmptyToken))
            callback(null)
            return
        }
        if (password.length < 6) {
            ToastHelper().show(activity, activity.getString(R.string.toastErrorPass))
            callback(null)
            return
        }

        FirebaseRepository().signInFirebase(activity, login, password) {
            if (it == null) {
                ToastHelper().show(activity, activity.getString(R.string.toastLoginError))
            }
            if (it != null && it.email != null) {
                userInfo.value = UserInfo(mail = it.email!!)
                getUser{}
            }

            user = it
            setLastWindow(Window.HOME)
            getUser{}
            callback(it)
        }
    }

    fun reg(activity: Activity, login: String, password: String, repPassword: String, name: String, callback: (FirebaseUser?) -> Unit) {
        if (login.isEmpty() || password.isEmpty()){
            ToastHelper().show(activity, activity.getString(R.string.toastEmptyToken))
            callback(null)
            return
        }
        if (password.length < 6) {
            ToastHelper().show(activity, activity.getString(R.string.toastErrorPass))
            callback(null)
            return
        }
        if (password != repPassword) {
            ToastHelper().show(activity, activity.getString(R.string.toastDontRepPass))
            callback(null)
            return
        }
        firebaseRepository.signUpFirebase(activity, login, password) {
            user = it
            if (it != null && it.email != null) {
                userInfo.value = UserInfo(mail = it.email!!)
                updateUser(UserInfo(mail = login, name = name))
                setLastWindow(Window.HOME)
                getUser{}
                callback(it)
            } else {
                ToastHelper().show(activity, activity.getString(R.string.toastErrorPass))
            }

        }
    }

    fun logout() {
        myAnnouncementList.value = listOf()
        announcementList.value = listOf()
        usersList.value = listOf()
        favoritesList.value = listOf()
        messageList.value = listOf()
        firebaseRepository.signOutFirebase()
    }

    fun updateUser(userInfo: UserInfo) {
        MainViewModel.userInfo.value = userInfo
        if (user == null) FirebaseRepository.checkAuth { user = it }
        if (user != null) {
            firebaseRepository.updateUserInfo(user!!, userInfo)
        }
    }

    fun getUser(callback: (UserInfo?) -> Unit) {
        if (user == null) FirebaseRepository.checkAuth { user = it }
        if (user != null) {
            FirebaseRepository().getUserInfo(user!!) {
                if (it != null) {
                    userInfo.value = it
                    if (userInfo.value.mail.isEmpty())
                        userInfo.value.mail = user!!.email!!
                }
                else if (user != null)
                    userInfo.value = UserInfo(mail = user!!.email!!)
                callback(it)
            }
        }
    }

    fun getUsers() {
        FirebaseRepository().getUsers {
            usersList.value = it
        }
    }

    fun setAnnouncement(item: Announcement) {
        if (user == null) FirebaseRepository.checkAuth { user = it }
        if (user != null) {
            try {
                if (item.id.isEmpty()) item.id = UUID.randomUUID().toString()
                if (item.userId.isEmpty()) item.userId = user!!.uid
                firebaseRepository.setAnnouncement(item)
                getAnnouncement{}
            } catch (e: Exception) {
                Log.e("TEST", e.toString())
            }
        }
    }

    fun removeAnnouncement(item: Announcement) {
        firebaseRepository.removeAnnouncement(item)
    }

    fun getAnnouncement(callback: () -> Unit) {
        if (user == null) FirebaseRepository.checkAuth { user = it }
        if (user != null) {
            announcementList.value = listOf()
            myAnnouncementList.value = listOf()

            FirebaseRepository().getAnnouncement {
                announcementList.value = it

                val myList = mutableListOf<Announcement>()
                announcementList.value.forEach { elem ->
                    if (elem.userId == user!!.uid) {
                        myList.add(elem)
                    }
                }
                myAnnouncementList.value = myList

                callback()
            }
        }
    }

    fun callPhone(context: Context, phone: String) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                    android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(context,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    42)
            }
        } else {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:${phone}")
            context.startActivity(intent)
        }
    }

    fun loadFavorites() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                bdRepository.all
                    .flowOn(Dispatchers.IO)
                    .catch { e -> Log.e(Const.LOG_TAG, e.toString()) }
                    .collect {
                        favoritesList.value = it
                    }
            }
        } catch (e: Exception) {
            Log.e(Const.LOG_TAG, e.toString())
        }
    }

    fun insertFavorites(announcement: Announcement) {
        bdRepository.insert(announcement)
    }

    fun deleteFavorites(announcement: Announcement) {
        bdRepository.delete(announcement)
    }

    fun setMessage(message: Message) {
        firebaseRepository.setMessage(message)
    }

    fun getMessages() {
        FirebaseRepository().getMessages {
            messageList.value = it
        }
    }
}