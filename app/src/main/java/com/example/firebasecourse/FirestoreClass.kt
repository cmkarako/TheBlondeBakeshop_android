package com.example.firebasecourse

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.firebasecourse.ui.dashboard.DashboardFragment
import com.example.firebasecourse.ui.notifications.OrdersFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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

            val sharedPreferences = activity.getSharedPreferences(Constants.BLONDEBAKESHOP_PREFERENCES, Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(Constants.LOGGED_IN_USERNAME, "${user.firstName} ${user.lastName}")
            editor.apply()

            when (activity) {
                is LoginActivity -> {
                    activity.userLoggedInSuccess(user)
                }
                is SettingsActivity -> {
                    activity.userDetailsSuccess(user)
                }
            }
        }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        firestore.collection(Constants.USERS).document(getCurrentUserID()).update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error while updating the user details.", e)
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?, imageType: String) {
        val storageReference : StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "." + Constants.getFileExtension(activity, imageFileUri)
        )
        storageReference.putFile(imageFileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when (activity) {
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddProductsActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                when(activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddProductsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, exception.message, exception)
            }
    }

    fun uploadProductDetails(activity: AddProductsActivity, productInfo: Product) {
        firestore.collection(Constants.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.uploadProductSuccess()
            }
            .addOnFailureListener {e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error uploading product.",
                    e
                )
            }
    }

    fun getProductList(fragment: Fragment){
        firestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList : ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.id = i.id
                    productsList.add(product)
                }

                when(fragment){
                    is OrdersFragment -> {
                        fragment.successProductsListFromFirestore(productsList)
                    }
                }
            }
    }

    fun deleteProductFromList(fragment: OrdersFragment, productId: String) {
        firestore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.refreshListAfterDeleteFromFirestore()
            }
            .addOnFailureListener { e->
                fragment.hideProgressdialog()
                Log.e(fragment.requireActivity().javaClass.simpleName, "Error deleting the product", e)
            }
    }

    fun getDashboardItemsList(fragment: DashboardFragment) {
        firestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())
                val productList : ArrayList<Product> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    if (product != null) {
                        product.id = i.id
                    }
                    if (product != null) {
                        productList.add(product)
                    }
                }
                fragment.hideProgressdialog()
                fragment.showDashboardProductList(productList)
            }
            .addOnFailureListener { e ->
                fragment.hideProgressdialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items")
            }
    }

    fun getProductDetails(activity: ProductDetailsActivity, productId: String) {
        firestore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    activity.getProductDetails(product)
                }
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting dashboard items")
            }
    }
}