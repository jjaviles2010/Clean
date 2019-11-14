package com.fiap18Mob.clean.view.usersList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiap18Mob.clean.R
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)


        recyclerView_Main.layoutManager = LinearLayoutManager(this)
        recyclerView_Main.adapter = MainAdapter()
    }
}
