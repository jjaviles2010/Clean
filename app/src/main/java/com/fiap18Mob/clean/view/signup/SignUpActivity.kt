package com.fiap18Mob.clean.view.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.fiap18Mob.clean.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity() {

    val signUpViewModel: SignUpViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        populateSpinner()

        etZipCode.setOnFocusChangeListener{ view, b -> searchAddress(b) }

        signUpViewModel.isLoading.observe(this, Observer {
            if (it == true) {
                containerLoading.visibility = View.VISIBLE
            } else {
                containerLoading.visibility = View.GONE
            }
        })

        signUpViewModel.messageError.observe(this, Observer {
            if(it != "")
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        signUpViewModel.address.observe(this, Observer {
            if(it != null)
                showAddressInfo()
        })
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


    private fun searchAddress(b: Boolean) {

        if(!b) {
            signUpViewModel.getAddress(etZipCode.text.toString())
        }

    }

    private fun showAddressInfo() {
        etStreetAddress.setText(signUpViewModel.address.value?.logradouro)
        etNeighborhood.setText(signUpViewModel.address.value?.bairro)
        etCity.setText(signUpViewModel.address.value?.localidade)
        val stateToSelect = resources.getStringArray(R.array.statesList).indexOf(signUpViewModel.address.value?.uf)
        spStates.setSelection(stateToSelect)
    }
}
