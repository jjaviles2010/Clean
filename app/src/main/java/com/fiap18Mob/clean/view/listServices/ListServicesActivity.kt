package com.fiap18Mob.clean.view.listServices

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiap18Mob.clean.BaseActivity
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.view.cleanerDetail.CleanerDetailActivity
import com.fiap18Mob.clean.view.serviceDetail.ServiceDetailActivity
import kotlinx.android.synthetic.main.activity_list_services.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.viewmodel.ext.android.viewModel

class ListServicesActivity : BaseActivity() {

    val listServicesViewModel: ListServicesViewModel by viewModel()


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
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
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
    }
}
