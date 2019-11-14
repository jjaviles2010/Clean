package com.fiap18Mob.clean.view.dataTableList

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.repository.UserRepositoryRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList

class DataTableListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_table_list)

        val listView = findViewById<ListView>(R.id.listView)


        listView.adapter = MyCustumerAdapter(this)

    }

    private class MyCustumerAdapter(context: Context): BaseAdapter() {

        private val myContext: Context

//        private val infos = arrayListOf<String>( "Gabriel", "Vilson", "Leando", "Javier" )
        val userRemoteRep = UserRepositoryRemote(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance())
        val infos = userRemoteRep.getUsersByProfile("CLEANER", onComplete = { }, onError = { })


        init {
                myContext = context
        }

        //Quantidade de linhas
        override fun getCount(): Int {
            return infos.size
        }

        //Cria a row para cada elemento
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(myContext)
            val rowLista = layoutInflater.inflate(R.layout.row_lista_dados, parent, false)

            val name = rowLista.findViewById<TextView>(R.id.textViewNameList)
            name.text = infos.get(position).nome

            val preco = rowLista.findViewById<TextView>(R.id.textViewSubNameList)
            preco.text = "R$ 100.00 reais"

            return rowLista
        }


        //nao sao usadas aqui, mas precisam ser chamadas para nao dar erro
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return ""
        }


    }

}
