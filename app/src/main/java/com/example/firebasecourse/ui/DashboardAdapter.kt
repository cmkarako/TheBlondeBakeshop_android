package com.example.firebasecourse.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasecourse.Constants
import com.example.firebasecourse.GlideLoader
import com.example.firebasecourse.Product
import com.example.firebasecourse.ProductDetailsActivity
import com.example.firebasecourse.databinding.DashboardListLayoutBinding

open class DashboardAdapter(private val context: Context, private var list: ArrayList<Product>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DashboardListLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return MyDashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyDashboardViewHolder){
            GlideLoader(context).loadUserPicture(model.image, holder.binding.productImageRecyclerview)
            holder.binding.productNameRecyclerview.text = model.title
            holder.binding.productPriceRecyclerview.text = "$${model.price}"
            holder.binding.productLayoutRecyclerview.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyDashboardViewHolder(val binding: DashboardListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}