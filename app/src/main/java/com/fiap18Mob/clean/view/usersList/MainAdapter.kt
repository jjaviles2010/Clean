package com.fiap18Mob.clean.view.usersList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.repository.UserRepositoryRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.user_row.view.*

class MainAdapter( val users: List<User> ): RecyclerView.Adapter<CustomViewHolder>() {

    //val userRemoteRep = UserRepositoryRemote(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance())
    //val infoClients = userRemoteRep.getUsersByProfile("CLEANER", onComplete = { }, onError = { })

    // number of items
    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_row, parent, false)

        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val userInfo = users.get(position)
        holder.view.textViewName.text = userInfo.nome
    }

}



class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}