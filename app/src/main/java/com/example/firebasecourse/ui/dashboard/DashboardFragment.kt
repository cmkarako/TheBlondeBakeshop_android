package com.example.firebasecourse.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.firebasecourse.FirestoreClass
import com.example.firebasecourse.Product
import com.example.firebasecourse.R
import com.example.firebasecourse.SettingsActivity
import com.example.firebasecourse.databinding.FragmentDashboardBinding
import com.example.firebasecourse.ui.BaseFragment
import com.example.firebasecourse.ui.DashboardAdapter

class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.settings_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_navigate_settings -> {
                        startActivity(Intent(activity, SettingsActivity::class.java))
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        getDashboardProductsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showDashboardProductList(dashboardList : ArrayList<Product>){
        hideProgressdialog()

        if (dashboardList.size>0) {
            binding.textDashboard.visibility = View.GONE
            //binding.dashboardItemsListRecyclerview.visibility = View.VISIBLE

            binding.dashboardItemsListRecyclerview.layoutManager = GridLayoutManager(activity, 2)
            binding.dashboardItemsListRecyclerview.setHasFixedSize(true)
            val adapter = DashboardAdapter(requireActivity(), dashboardList)
            binding.dashboardItemsListRecyclerview.adapter = adapter
        }
    }

    fun getDashboardProductsList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getDashboardItemsList(this@DashboardFragment)
    }
}