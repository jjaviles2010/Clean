package com.fiap18Mob.clean.view.usersList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.view.cleanerDetail.CleanerDetailActivity
import kotlinx.android.synthetic.main.activity_users_list.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.viewmodel.ext.android.viewModel

class UsersListViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val usersListViewModel: UsersListViewModel by viewModel()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)


        usersListViewModel.getUserList()

        usersListViewModel.isLoading.observe(this, Observer {
            if (it == true) {
                containerLoading.visibility = View.VISIBLE
            } else {
                containerLoading.visibility = View.GONE
            }
        })

        usersListViewModel.messageError.observe(this, Observer {
            if (it != "") {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        usersListViewModel.users.observe(this, Observer {
            recyclerView_Main.adapter = UsersListAdapter(
                it
            ){
                val cleanerDetailActivity = Intent(this, CleanerDetailActivity::class.java)
                cleanerDetailActivity.putExtra("USER", it)
                startActivity(cleanerDetailActivity)
                finish()
            }

            recyclerView_Main.layoutManager = LinearLayoutManager(this)

        })


    }


}
