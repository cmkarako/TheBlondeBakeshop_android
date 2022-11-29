package com.example.firebasecourse

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: Dialog
    private var selectedImageUri: Uri? = null


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