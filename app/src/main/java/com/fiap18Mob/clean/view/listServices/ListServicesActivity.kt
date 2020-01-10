package com.fiap18Mob.clean.view.listServices

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiap18Mob.clean.BaseActivity
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.view.serviceDetail.ServiceDetailActivity
import com.fiap18mob.mylib.CustomToast
import kotlinx.android.synthetic.main.activity_list_services.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ListServicesActivity : BaseActivity() {

    val listServicesViewModel: ListServicesViewModel by viewModel()
    val customToast: CustomToast by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_services)

        listServicesViewModel.getCleaningServices()
        listServicesViewModel.isLoading.observe(this, Observer {
            if (it == true) {
                containerLoading.visibility = View.VISIBLE
            } else {
                containerLoading.visibility = View.GONE
            }
        })

        listServicesViewModel.messageError.observe(this, Observer {
            if(it != "") {
                customToast.showToast(this, it, CustomToast.ERROR)
            }
        })
        listServicesViewModel.services.observe(this, Observer {
            rvListServices.adapter = ListServicesAdapter(
                it
            ) {
                val serviceDetailActivity = Intent(this, ServiceDetailActivity::class.java)
                serviceDetailActivity.putExtra("SERVICE", it)
                startActivity(serviceDetailActivity)
                finish()
            }

            rvListServices.layoutManager = LinearLayoutManager(this)
        })

        listServicesViewModel.isDeleted.observe(this, Observer {
            if (it == true) {
                customToast.showToast(this, getString(R.string.serviceDeletedMsg), CustomToast.INFO)
            }
        })

        setRecyclerViewItemTouchListener()
    }

    private fun setRecyclerViewItemTouchListener() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    listServicesViewModel.deleteCleaningServices(listServicesViewModel.services.value!![position])
                    listServicesViewModel.services.value?.removeAt(position)
                    rvListServices.adapter!!.notifyItemRemoved(position)
                }
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rvListServices)
    }
}
