package com.example.firebasecourse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.firebasecourse.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity() {

    private lateinit var binding : ActivitySettingsBinding
    private lateinit var userDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.editSettings.setOnClickListener {
            Toast.makeText(this, "Edit button clicked", Toast.LENGTH_LONG).show()
            val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.MOBILE, userDetails.mobile)
            Log.d("Mobile", "Testing ${userDetails.mobile}")
            intent.putExtra(Constants.IMAGE, userDetails.image)
            intent.putExtra(Constants.FIRST_NAME, userDetails.firstName)
            intent.putExtra(Constants.LAST_NAME, userDetails.lastName)
            intent.putExtra(Constants.EMAIL, userDetails.email)
            startActivity(intent)
        }
    }



//    private fun setUpActionBar() {
//        setSupportActionBar(binding.settingsToolbar)
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
//        }
//        binding.settingsToolbar.setNavigationOnClickListener { onBackPressed() }
//    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        userDetails = user
        hideProgressDialog()
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, binding.profileImageSettings)
        binding.textName.text = "${user.firstName} ${user.lastName}"
        binding.textViewPhone.text = user.mobile.toString()
        binding.textViewEmail.text = user.email

    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

}