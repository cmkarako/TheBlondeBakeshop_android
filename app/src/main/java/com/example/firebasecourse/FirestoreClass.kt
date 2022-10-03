package com.example.firebasecourse

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val firestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        firestore.collection(Constants.USERS).document(userInfo.id).set(userInfo, SetOptions.merge()).addOnSuccessListener {
            activity.userRegistrationSucess()
        }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while registering the user.", e )
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        firestore.collection(Constants.USERS).document(getCurrentUserID()).get().addOnSuccessListener { document ->
            Log.i(activity.javaClass.simpleName, document.toString())
            val user = document.toObject(User::class.java)!!
            when (activity) {
                is LoginActivity -> {
                    activity.userLoggedInSuccess(user)
                }
            }
        }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

}