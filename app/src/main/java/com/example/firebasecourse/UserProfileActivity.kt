package com.example.firebasecourse

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.firebasecourse.databinding.ActivityUserProfileBinding
import java.io.IOException
import java.util.jar.Manifest

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private var selectedImageUri: Uri? = null
    private var userProfileImageURL : String = ""
   // private lateinit var userDetails: User

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photo = intent.getStringExtra(Constants.IMAGE)
        if (photo != null) {
            GlideLoader(this@UserProfileActivity).loadUserPicture(photo, binding.userImageInput)
        }

        binding.userImageInput.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding.firstNameInput.setText(intent.getStringExtra(Constants.FIRST_NAME))
        binding.firstNameInput.isEnabled = true

        binding.lastNameInput.setText(intent.getStringExtra(Constants.LAST_NAME))
        binding.lastNameInput.isEnabled = true

        binding.editTextTextEmailAddressInput.setText(intent.getStringExtra(Constants.EMAIL))

        binding.editTextPhoneInput.setText(intent.getLongExtra(Constants.MOBILE, 0).toString())

        binding.saveProfile.setOnClickListener {
            if (validateUserProfileDetails()) {
                showProgressDialog(resources.getString(R.string.please_wait))
                if(selectedImageUri != null) {
                    FirestoreClass().uploadImageToCloudStorage(this, selectedImageUri, Constants.PROFILE_IMAGE)
                }
                else {
                    updateUserProfileDetails()
                }
            }
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when(view.id) {
                R.id.save_profile -> {
                    if (validateUserProfileDetails()) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        if(selectedImageUri != null) {
                            FirestoreClass().uploadImageToCloudStorage(this, selectedImageUri, Constants.PROFILE_IMAGE)
                        }
                        else {
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails() {
        //FirestoreClass().uploadImageToCloudStorage(this, selectedImageUri)
        val userHashMap = HashMap<String, Any>()
        val firstName = binding.firstNameInput.text.toString().trim { it <= ' ' }
        val lastName = binding.lastNameInput.text.toString().trim { it <= ' ' }
        val mobileNumber = binding.editTextPhoneInput.text.toString().trim { it <= ' ' }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
            userHashMap[Constants.FIRST_NAME] = firstName
            userHashMap[Constants.LAST_NAME] = lastName
        }

        if(userProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = userProfileImageURL
        }
        userHashMap[Constants.COMPLETE_PROFILE] = 1
        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@UserProfileActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser(this)
            } else {
                Toast.makeText(this, "Read Storage Permission is Denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }



fun showImageChooser(activity: Activity) {
    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startForResult.launch(galleryIntent)
}
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            Log.e("ResultCode", "Result Code works.")
            if (intent != null) {
                try {
                    var user_photo : ImageView = findViewById(R.id.user_image_input)
                    Log.e("Image Success", "Image Success.")
                    selectedImageUri = result.data!!.data!!
                    GlideLoader(this).loadUserPicture(selectedImageUri!!, user_photo)
                    Glide.with(this).load(selectedImageUri).into(user_photo)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("Image Failed", "Image Selection Failed.")
                    Toast.makeText(this@UserProfileActivity, "Image Selection Failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.e("Image Failed", "Result code not Ok.")
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.editTextPhoneInput.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar("Please enter mobile number", true)
                false
            } else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String) {
        userProfileImageURL = imageURL
        updateUserProfileDetails()
    }

}