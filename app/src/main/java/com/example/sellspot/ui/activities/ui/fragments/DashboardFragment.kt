package com.example.sellspot.ui.activities.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sellspot.R
import com.example.sellspot.databinding.FragmentDashboardBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.Product
import com.example.sellspot.ui.activities.ui.activities.CartListActivity
import com.example.sellspot.ui.activities.ui.activities.ProductDetailsActivity
import com.example.sellspot.ui.activities.ui.activities.SettingsActivity
import com.example.sellspot.utils.Constants
import com.myshoppal.ui.adapters.DashboardItemsListAdapter

class DashboardFragment : BaseFragment()  {

    private var _binding: FragmentDashboardBinding? = null

    private lateinit var allProductsList: ArrayList<Product>
    private lateinit var adapter: DashboardItemsListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If we want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(true)
        showFullPageAd()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ...

        // Initialize allProductsList with an empty list
        allProductsList = ArrayList()

        // Initialize the adapter with an empty list
        adapter = DashboardItemsListAdapter(requireActivity(), ArrayList())
        binding.rvDashboardItems.adapter = adapter

        // Call the function to fetch all products from Firestore
        getDashboardItemsList()

        // Set up the SearchView listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchProducts(it) }
                return true
            }
        })
    }

    private fun searchProducts(query: String) {
        val filteredList = allProductsList.filter {
            it.title.contains(query, ignoreCase = true)
        }.toMutableList() // Convert the filtered list to a MutableList
        Log.d("DashboardFragment", "Filtering for query: $query, Results: $filteredList")
        adapter.list = filteredList as ArrayList<Product>
        adapter.notifyDataSetChanged()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.action_settings -> {

                startActivity(Intent(activity, SettingsActivity::class.java))

                return true
            }

            // TODO Step 9: Handle the click event of Cart action item.
            // START
            R.id.action_cart -> {
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
            // END
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
//        showFullPageAd()
        getDashboardItemsList()
    }

    /**
     * A function to get the dashboard items list from cloud firestore.
     */
    private fun getDashboardItemsList() {
        // Show the progress dialog.
        //showProgressDialog(resources.getString(R.string.please_wait))

        FirebaseClass().getDashboardItemsList(this@DashboardFragment)
    }

    /**
     * A function to get the success result of the dashboard items from cloud firestore.
     *
     * @param dashboardItemsList List of products fetched from Firestore.
     */
    fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>) {
        // Update the allProductsList with the received dashboardItemsList
        allProductsList = dashboardItemsList

        // Hide the progress dialog.
        // hideProgressDialog()

        if (dashboardItemsList.size > 0) {
            // Show the RecyclerView and hide the "No Items Found" TextView
            binding.rvDashboardItems.visibility = View.VISIBLE
            binding.tvNoDashboardItemsFound.visibility = View.GONE

            binding.rvDashboardItems.layoutManager = GridLayoutManager(activity, 2)
            binding.rvDashboardItems.setHasFixedSize(true)

            // Update the adapter with the received dashboardItemsList
            adapter.list = dashboardItemsList
            adapter.notifyDataSetChanged()

            adapter.setOnClickListener(object : DashboardItemsListAdapter.OnClickListener {
                override fun onClick(position: Int, product: Product) {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, product.user_id)
                    startActivity(intent)
                }
            })
        } else {
            // Show the "No Items Found" TextView and hide the RecyclerView
            binding.rvDashboardItems.visibility = View.GONE
            binding.tvNoDashboardItemsFound.visibility = View.VISIBLE
        }
    }


    private fun showFullPageAd() {
        // Inflate the custom ad layout
        val adView = layoutInflater.inflate(R.layout.add_layout, null)

        // Show the ad layout as a dialog
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(adView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        // Set a click listener for the close button
        val closeButton = adView.findViewById<ImageButton>(R.id.iv_close)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }


}