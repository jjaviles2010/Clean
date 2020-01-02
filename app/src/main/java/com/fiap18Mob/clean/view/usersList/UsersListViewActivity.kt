package com.fiap18Mob.clean.view.usersList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.view.cleanerDetail.CleanerDetailActivity
import com.fiap18mob.mylib.CustomToast
import kotlinx.android.synthetic.main.activity_users_list.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class UsersListViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val usersListViewModel: UsersListViewModel by viewModel()
        val customToast: CustomToast by inject()

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
                customToast.showToast(this, it, CustomToast.ERROR)
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
