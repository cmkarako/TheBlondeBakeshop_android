package com.example.firebasecourse.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasecourse.*
import com.example.firebasecourse.databinding.ItemListLayoutBinding
import com.example.firebasecourse.ui.notifications.OrdersFragment

open class ProductListAdapter(private val context: Context, private var list: ArrayList<Product>, private val fragment: OrdersFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemListLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding)
//        return MyViewHolder(
//            LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false)
//        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            GlideLoader(context).loadUserPicture(model.image, holder.binding.productImageRecyclerview)
            holder.binding.productNameRecyclerview.text = model.title
            holder.binding.productPriceRecyclerview.text = "$${model.price}"

            holder.binding.deleteIcon.setOnClickListener {
                fragment.deleteProductFromFirestore(model.id)
            }
            holder.binding.itemRecyclerview.setOnClickListener {
                fragment.viewItemDetails()
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val binding: ItemListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}