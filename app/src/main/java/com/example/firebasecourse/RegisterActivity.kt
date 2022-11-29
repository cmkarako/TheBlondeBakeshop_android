package com.example.firebasecourse

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //setUpActionBar()

        val registerUser : Button = findViewById(R.id.buttonToRegister)
        registerUser.setOnClickListener {
         //   validateRegisterDetails()
            registerUser()
        }
    }

//    private fun setUpActionBar() {
//        val toolbar: Toolbar = findViewById(R.id.toolbar_register_activity)
//        setSupportActionBar(toolbar)
//
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
//            actionBar.setTitle("Return to Login")
//        }
//        toolbar.setNavigationOnClickListener { onBackPressed() }
//    }

    private fun validateRegisterDetails() : Boolean {

        val firstName : String = findViewById<EditText?>(R.id.editTextFirstName).text.toString().trim { it <= ' ' }
        val lastName : String = findViewById<EditText?>(R.id.editTextLastName).text.toString().trim { it <= ' ' }
        val email : String = findViewById<EditText?>(R.id.editTextTextEmailAddress).text.toString().trim { it <= ' ' }
        val password : String = findViewById<EditText>(R.id.editTextTextPassword2).text.toString().trim { it <= ' ' }
        val password2 : String = findViewById<EditText>(R.id.editTextTextPassword3).text.toString().trim { it <= ' ' }
        val terms : CheckBox = findViewById(R.id.termsCheckBox)

        return when {
            TextUtils.isEmpty(firstName) -> {
                showErrorSnackBar("Please enter first name", true)
                false
            }
            TextUtils.isEmpty(lastName) -> {
                showErrorSnackBar("Please enter last name", true)
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email", true)
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password", true)
                false
            }
            TextUtils.isEmpty(password2) -> {
                showErrorSnackBar("Please confirm password", true)
                false
            }
            password != password2 -> {
                showErrorSnackBar("Passwords do not match", true)
                false
            }
            !terms.isChecked -> {
                showErrorSnackBar("Please accept the terms and conditions.", true)
                false
            }
            else -> {
                //showErrorSnackBar("Thank you for registering", false)
                true
            }
        }
    }

    private fun registerUser() {
        if (validateRegisterDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            val email: String = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString().trim { it <= ' '}
            val password : String = findViewById<EditText>(R.id.editTextTextPassword2).text.toString().trim { it <= ' ' }
            val firstName : String = findViewById<EditText>(R.id.editTextFirstName).text.toString().trim { it <= ' ' }
            val lastName : String = findViewById<EditText>(R.id.editTextLastName).text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val firebaseUser : FirebaseUser = task.result!!.user!!
                        val user = User(
                            firebaseUser.uid,
                            firstName,
                            lastName,
                            email
                        )
                        FirestoreClass().registerUser(this@RegisterActivity, user)
//                        showErrorSnackBar("You are registered successfully. Your user id is ${firebaseUser.uid}", false)
//                        FirebaseAuth.getInstance().signOut()
//                        finish()
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
            )
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun userRegistrationSucess() {
        hideProgressDialog()
        Toast.makeText(this@RegisterActivity, "Registered Successfully!", Toast.LENGTH_SHORT).show()
    }

}