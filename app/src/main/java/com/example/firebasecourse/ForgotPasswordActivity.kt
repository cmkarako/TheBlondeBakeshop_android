package com.example.firebasecourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setUpActionBar()
    }

    private fun setUpActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_reset_activity)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar.setTitle("")
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val resetButton : Button = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            val email: String = findViewById<EditText>(R.id.emailReset).text.toString().trim { it <= ' ' }
            if (email.isEmpty()) {
                showErrorSnackBar("Please enter email", true)
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        Toast.makeText(this@ForgotPasswordActivity, "Email sent successfully!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
            }
        }

    }

}