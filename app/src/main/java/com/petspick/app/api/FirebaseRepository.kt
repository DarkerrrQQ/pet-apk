package com.petspick.app.api

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.petspick.app.data.storage.models.Announcement
import com.petspick.app.utils.Const
import com.petspick.app.utils.Const.Companion.ANNOUNCEMENT
import com.petspick.app.utils.Const.Companion.DATE
import com.petspick.app.utils.Const.Companion.FROM
import com.petspick.app.utils.Const.Companion.ID
import com.petspick.app.utils.Const.Companion.IMAGE
import com.petspick.app.utils.Const.Companion.MAIL
import com.petspick.app.utils.Const.Companion.MESSAGE
import com.petspick.app.utils.Const.Companion.NAME
import com.petspick.app.utils.Const.Companion.PHONE
import com.petspick.app.utils.Const.Companion.TO
import com.petspick.app.utils.Const.Companion.USERS
import com.petspick.app.utils.Const.Companion.USER_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor () {

    companion object {
        private var auth: FirebaseAuth = Firebase.auth

        fun checkAuth(callback: (FirebaseUser?) -> Unit) {
            callback(auth.currentUser)
        }
    }

    private var firestore: FirebaseFirestore = Firebase.firestore

    fun signInFirebase(activity: Activity, email: String, password: String, callback: (FirebaseUser?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        callback(user)
                    } else {
                        callback(null)
                    }
                }
        }
    }

    fun signUpFirebase(activity: Activity, email: String, password: String, callback: (FirebaseUser?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        callback(user)
                    } else {
                        callback(null)
                    }
                }
        }
    }

    fun signOutFirebase() {
        auth.signOut()
    }

    fun updateUserInfo(user: FirebaseUser, userInfo: UserInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            firestore.collection(USERS)
                .document(user.uid)
                .set(mapOf(ID to user.uid, NAME to userInfo.name, PHONE to userInfo.phone, IMAGE to userInfo.image, MAIL to userInfo.mail))
        }
    }

    fun getUserInfo(user: FirebaseUser, callback: (UserInfo?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            firestore.collection(USERS)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id == user.uid) {
                            callback(
                                UserInfo(
                                    document.data[ID].toString(),
                                    document.data[MAIL].toString(),
                                    document.data[NAME].toString(),
                                    document.data[PHONE].toString(),
                                    document.data[IMAGE].toString()
                                )
                            )
                            return@addOnSuccessListener
                        }
                    }
                    callback(null)
                }
                .addOnFailureListener { exception ->
                    Log.e(Const.LOG_TAG, "Error getting documents.", exception)
                }
        }
    }

    fun getUsers(callback: (List<UserInfo>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = mutableListOf<UserInfo>()

            firestore.collection(USERS)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        list.add(
                            UserInfo(
                                document.data[ID].toString(),
                                document.data[MAIL].toString(),
                                document.data[NAME].toString(),
                                document.data[PHONE].toString(),
                                document.data[IMAGE].toString()
                            )
                        )
                    }
                    callback(list)
                }
                .addOnFailureListener { exception ->
                    Log.e(Const.LOG_TAG, "Error getting documents.", exception)
                }
        }
    }

    fun setAnnouncement(item: Announcement) {
        CoroutineScope(Dispatchers.IO).launch {
            firestore.collection(Const.ANNOUNCEMENT)
                .document(item.id).set(
                    mapOf(
                        Const.ID to item.id,
                        Const.USER_ID to item.userId,
                        Const.NAME to item.name,
                        Const.SEX to item.sex,
                        Const.AGE to item.age,
                        Const.TYPE to item.type,
                        Const.ADDRESS to item.address,
                        Const.DESCRIPTION to item.description,
                        Const.PRICE to item.price,
                        Const.IMAGE to item.image,
                    )
                )
        }
    }

    fun removeAnnouncement(announcement: Announcement) {
        firestore.collection(Const.ANNOUNCEMENT).document(announcement.id)
            .delete()
            .addOnSuccessListener { Log.d(Const.LOG_TAG, "${announcement.id} successfully deleted!") }
            .addOnFailureListener { e -> Log.w(Const.LOG_TAG, "${announcement.id} Error deleting document", e) }
    }

    fun getAnnouncement(callback: (List<Announcement>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = mutableListOf<Announcement>()
            firestore.collection(Const.ANNOUNCEMENT)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        list.add(
                            Announcement(
                                id = document[ID].toString(),
                                userId = document[USER_ID].toString(),
                                name = document[NAME].toString(),
                                sex = document[Const.SEX].toString(),
                                age = document[Const.AGE].toString(),
                                type = document[Const.TYPE].toString(),
                                address = document[Const.ADDRESS].toString(),
                                description = document[Const.DESCRIPTION].toString(),
                                price = document[Const.PRICE].toString(),
                                image = document[IMAGE].toString(),
                            )
                        )
                    }
                    callback(list)
                }
                .addOnFailureListener { exception ->
                    Log.w(Const.LOG_TAG, "Error getting documents.", exception)
                }
        }
    }

    fun setMessage(item: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            firestore.collection(MESSAGE)
                .document(item.id).set(
                    mapOf(
                        ID to item.id,
                        FROM to item.from,
                        TO to item.to,
                        MESSAGE to item.message,
                        DATE to item.date,
                        ANNOUNCEMENT to item.announcement
                    )
                )
        }
    }

    fun getMessages(callback: (List<Message>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = mutableListOf<Message>()
            firestore.collection(MESSAGE)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        list.add(
                            Message(
                                id = document[ID].toString(),
                                from = document[FROM].toString(),
                                to = document[TO].toString(),
                                message = document[MESSAGE].toString(),
                                date = document[DATE].toString(),
                                announcement = document[ANNOUNCEMENT].toString(),

                            )
                        )
                    }
                    callback(list)
                }
                .addOnFailureListener { exception ->
                    Log.w(Const.LOG_TAG, "Error getting documents.", exception)
                }
        }
    }
}

data class UserInfo(
    var id: String = "",
    var mail: String = "",
    var name: String = "",
    var phone: String = "",
    var image: String = ""
)

data class Message(
    var id: String = "",
    var to: String = "",
    var from: String = "",
    var announcement: String = "",
    var message: String = "",
    var date: String = ""
)
