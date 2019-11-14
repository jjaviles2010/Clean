package com.fiap18Mob.clean.view.usersList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fiap18Mob.clean.R
import kotlinx.android.synthetic.main.user_row.view.*


class MainAdapter(): RecyclerView.Adapter<CustomViewHolder>() {

    val infoClients = listOf<String> ("Gabriel", "Vilson", "Javier", "Leandro")



    // number of items
    override fun getItemCount(): Int {
        return infoClients.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_row, parent, false)

        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val userInfo = infoClients.get(position)
        holder.view.textViewName.text = userInfo
    }

}



class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}