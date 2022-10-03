package com.example.firebasecourse

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: Dialog


    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.red))
        } else {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.green))
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String) {

        progressDialog = Dialog(this)

        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.findViewById<TextView>(R.id.pleaseWait).text = text
//        val progressText : TextView = findViewById(R.id.pleaseWait)
//        progressText.text = text
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)

        progressDialog.show()
    }

    fun hideProgressDialog() {
        progressDialog.dismiss()
    }

}