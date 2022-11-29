package com.example.firebasecourse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.firebasecourse.databinding.ActivityProductDetailsBinding

class ProductDetailsActivity : BaseActivity() {

    private lateinit var binding : ActivityProductDetailsBinding
    private var productId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.returnToProductsButton.setOnClickListener {
//            val intent = Intent(this, DashboardActivity::class.java)
//            startActivity(intent)
            onBackPressed()
        }

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            productId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        retrieveProductDetails()

    }

    fun retrieveProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this, productId)
    }

    fun getProductDetails(product: Product) {
        hideProgressDialog()
        GlideLoader(this@ProductDetailsActivity).loadUserPicture(
            product.image, binding.productImageDetails
        )
        binding.productTitleDetails.setText(product.title)
        binding.productDescriptionDetails.setText(product.description)
        Log.i("Product Details", product.description)
        binding.productPriceDetails.setText("$ ${product.price}")
        binding.productQuantityDetails.setText(product.stock_quantity)
    }

}