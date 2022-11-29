package com.example.firebasecourse

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS: String = "users"
    const val BLONDEBAKESHOP_PREFERENCES : String = "BlondeBakeShopPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val MOBILE: String = "mobile"
    const val IMAGE : String = "image"
    const val PROFILE_IMAGE : String = "User_Profile_Image"
    const val COMPLETE_PROFILE : String = "profileCompleted"
    const val FIRST_NAME : String = "firstName"
    const val LAST_NAME : String = "lastName"
    const val EMAIL: String = "email"

    const val PRODUCT_IMAGE : String = "Product_Image"
    const val PRODUCTS : String = "products"

    const val USER_ID : String = "user_id"

    const val EXTRA_PRODUCT_ID : String = "extra_product_id"

    fun getFileExtension(activity: Activity, uri: Uri?) : String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

//    fun showImageChooser(activity: Activity) {
//        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
////        activity.registerForActivityResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
//        startForResult.launch(galleryIntent)
//    }

}