package com.fiap18Mob.clean.view.usersList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.model.User
import kotlinx.android.synthetic.main.users_list_item.view.*

class UsersListAdapter(
    val users: List<User>,
    val clickListener: (User) -> Unit
): RecyclerView.Adapter<UsersListAdapter.UserViewHolder>() {

    //val userRemoteRep = UserRepositoryRemote(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance())
    //val infoClients = userRemoteRep.getUsersByProfile("CLEANER", onComplete = { }, onError = { })

    // number of items
    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.users_list_item, parent, false)

        return UserViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userInfo = users.get(position)
        holder.bindView(userInfo, clickListener)

    }

    class UserViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bindView(userInfo: User,
                     clickListener: (User) -> Unit) = with(itemView) {
            tvUserName.text = userInfo.nome
            tvDocument.text = userInfo.cpf
            tvPrice.text = userInfo.hourValue.toString()
            setOnClickListener { clickListener(userInfo) }
        }
    }
}