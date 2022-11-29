package com.example.firebasecourse

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.firebasecourse.databinding.ActivityAddProductsBinding
import java.io.IOException

class AddProductsActivity : BaseActivity() {

    lateinit var binding: ActivityAddProductsBinding
    private var selectedImageUri: Uri? = null
    private var productImageURL : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addProductIcon.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser(this@AddProductsActivity)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE)
            }
        }

        binding.addProductButton.setOnClickListener {
            if (validateProductDetails()) {
                uploadProductImage()
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
                    var productImage : ImageView = binding.addedProductImage
                    Log.e("Image Success", "Image Success.")
                    selectedImageUri = result.data!!.data!!
                    GlideLoader(this).loadUserPicture(selectedImageUri!!, productImage)
                    binding.addProductIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_edit_24))
                    //Glide.with(this).load(selectedImageUri).into(productImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("Image Failed", "Image Selection Failed.")
                    Toast.makeText(this@AddProductsActivity, "Image Selection Failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.e("Image Failed", "Result code not Ok.")
        }
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

    private fun validateProductDetails() : Boolean {
        return when {
            selectedImageUri == null -> {
                showErrorSnackBar("Please select an image", true)
                false
            }
            TextUtils.isEmpty(binding.etProductTitle.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter product title", true)
                false
            }
            TextUtils.isEmpty(binding.etProductDescription.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter product title", true)
                false
            }
            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter product title", true)
                false
            }
            TextUtils.isEmpty(binding.etProductQty.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter product title", true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun uploadProductImage() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(this, selectedImageUri, Constants.PRODUCT_IMAGE)
    }

    fun imageUploadSuccess(imageURL: String) {
        productImageURL = imageURL
        uploadProductDetails()
    }

    private fun uploadProductDetails() {
        val username = this.getSharedPreferences(
            Constants.BLONDEBAKESHOP_PREFERENCES,
            Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME, "")!!

        val product = Product(
            FirestoreClass().getCurrentUserID(),
            username,
            binding.etProductTitle.text.toString().trim { it <= ' ' },
            binding.etProductPrice.text.toString().trim { it <= ' ' },
            binding.etProductDescription.text.toString().trim { it <= ' ' },
            binding.etProductQty.text.toString().trim { it <= ' ' },
            productImageURL
        )
        FirestoreClass().uploadProductDetails(this, product)
    }

    fun uploadProductSuccess() {
        hideProgressDialog()
        Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_LONG).show()
        finish()
    }

}