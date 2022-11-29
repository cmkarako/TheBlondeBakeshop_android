package com.example.firebasecourse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registerButton : Button = findViewById(R.id.button2)
        val logInButton : Button = findViewById(R.id.button)
        val resetPasswordButton : Button = findViewById(R.id.forgotPassword)
        registerButton.setOnClickListener(this)
        logInButton.setOnClickListener(this)
        resetPasswordButton.setOnClickListener(this)
//        registerButton.setOnClickListener {
//            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//            startActivity(intent)
//        }

    }

    fun userLoggedInSuccess(user: User) {
        hideProgressDialog()
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        if (user.profileCompleted ==0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.FIRST_NAME, user.firstName)
            intent.putExtra(Constants.IMAGE, user.image)
            intent.putExtra(Constants.LAST_NAME, user.lastName)
            intent.putExtra(Constants.EMAIL, user.email)
            intent.putExtra(Constants.MOBILE, user.mobile)
            startActivity(intent)
        } else {
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.forgotPassword -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.button -> {
                    logInRegisteredUser()
                }
                R.id.button2 -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails() : Boolean {
        val username : String = findViewById<EditText>(R.id.username).text.toString().trim { it <= ' ' }
        val password : String = findViewById<EditText>(R.id.editTextTextPassword).text.toString().trim { it <= ' ' }
        return when {
            TextUtils.isEmpty(username) -> {
                showErrorSnackBar("Please enter email", true)
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password", true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))

            val email : String = findViewById<EditText>(R.id.username).text.toString().trim { it <= ' ' }
            val password : String = findViewById<EditText>(R.id.editTextTextPassword).text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    FirestoreClass().getUserDetails(this@LoginActivity)
//                    showErrorSnackBar("You are logged in successfully", false)
                } else {
                    hideProgressDialog()
                    showErrorSnackBar(task.exception!!.message.toString(), true)
                }
            }
        }
    }



}