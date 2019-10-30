package com.fiap18Mob.clean.view.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.fiap18Mob.clean.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        populateSpinner()

    }

    private fun populateSpinner() {

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.statesList,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spStates.adapter = adapter
    }
}
