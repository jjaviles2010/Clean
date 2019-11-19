package com.fiap18Mob.clean.view.usersList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiap18Mob.clean.R
import kotlinx.android.synthetic.main.activity_recycler_view.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.viewmodel.ext.android.viewModel

class RecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val recyclerViewModel: RecyclerViewModel by viewModel()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)


        recyclerViewModel.getUserList()
        recyclerViewModel.isLoading.observe(this, Observer {
            if (it == true) {
                containerLoading.visibility = View.VISIBLE
            } else {
                containerLoading.visibility = View.GONE
            }
        })

        recyclerViewModel.messageError.observe(this, Observer {
            if (it != "") {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        recyclerViewModel.users.observe(this, Observer {
            recyclerView_Main.adapter = MainAdapter(
                it
            )

            recyclerView_Main.layoutManager = LinearLayoutManager(this)

        })


    }


}
