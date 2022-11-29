package com.example.firebasecourse.ui.notifications

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasecourse.AddProductsActivity
import com.example.firebasecourse.FirestoreClass
import com.example.firebasecourse.Product
import com.example.firebasecourse.R
import com.example.firebasecourse.databinding.FragmentNotificationsBinding
import com.example.firebasecourse.ui.BaseFragment
import com.example.firebasecourse.ui.ProductListAdapter

class OrdersFragment : BaseFragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_product_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_product -> {
                        startActivity(Intent(activity, AddProductsActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun successProductsListFromFirestore(productList: ArrayList<Product>){
        hideProgressdialog()

        if(productList.size > 0) {
            binding.noProductsFound.visibility = View.GONE
            binding.itemsRecyclerview.visibility = View.VISIBLE

            binding.itemsRecyclerview.layoutManager = LinearLayoutManager(activity)
            binding.itemsRecyclerview.setHasFixedSize(true)

            val adapter = ProductListAdapter(requireActivity(), productList, this)
            binding.itemsRecyclerview.adapter = adapter
        } else {
            binding.noProductsFound.visibility = View.VISIBLE
            binding.itemsRecyclerview.visibility = View.GONE
        }
    }

    private fun getProductListFromFirestore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFirestore()
    }

    fun deleteProductFromFirestore(productID: String) {
        Toast.makeText(requireActivity(), "make a toast message", Toast.LENGTH_LONG).show()
        confirmDeleteProduct(productID)
    }

    fun refreshListAfterDeleteFromFirestore() {
        hideProgressdialog()
        Toast.makeText(requireActivity(), "Prodcut was deleted", Toast.LENGTH_LONG).show()
        getProductListFromFirestore()
    }

    private fun confirmDeleteProduct(productID: String){
        val alert = AlertDialog.Builder(requireActivity())
        alert.setTitle(resources.getString(R.string.delete_confirm))
        alert.setMessage(resources.getString(R.string.delete_confirm_message))
        alert.setIcon(R.drawable.warning_icon)

        alert.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _->
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().deleteProductFromList(this, productID)
            dialogInterface.dismiss()
        }
        alert.setNegativeButton(resources.getString(R.string.no)){ dialogInterface, _->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = alert.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.GREEN)
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.RED)
    }

    fun viewItemDetails() {
        Toast.makeText(requireActivity(), "Item has been clicked", Toast.LENGTH_LONG).show()
    }
}