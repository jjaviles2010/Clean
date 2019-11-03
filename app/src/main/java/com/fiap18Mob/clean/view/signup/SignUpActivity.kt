package com.fiap18Mob.clean.view.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.utils.Mask
import com.fiap18Mob.clean.utils.isValidEmail
import com.fiap18Mob.clean.utils.validate
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity() {

    val signUpViewModel: SignUpViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        populateSpinner()

        etCPF.addTextChangedListener(Mask.mask("###.###.###-##", etCPF))

        etZipCode.addTextChangedListener(Mask.mask("#####-###", etZipCode))
        etZipCode.setOnFocusChangeListener { view, b -> searchAddress(b) }

        etPhone.addTextChangedListener(Mask.mask("(##) #####-####", etPhone))

        signUpViewModel.isLoading.observe(this, Observer {
            if (it == true) {
                containerLoading.visibility = View.VISIBLE
            } else {
                containerLoading.visibility = View.GONE
            }
        })

        signUpViewModel.messageError.observe(this, Observer {
            if (it != "")
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        signUpViewModel.address.observe(this, Observer {
            if (it != null)
                showAddressInfo()
        })

        btnSendSignUp.setOnClickListener {
            sendSignUp()
        }
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
            signUpViewModel.getAddress(Mask.replaceChars(etZipCode.text.toString()))
        }
    }

    private fun showAddressInfo() {
        etStreetAddress.setText(signUpViewModel.address.value?.logradouro)
        etNeighborhood.setText(signUpViewModel.address.value?.bairro)
        etCity.setText(signUpViewModel.address.value?.localidade)
        val stateToSelect = resources.getStringArray(R.array.statesList).indexOf(signUpViewModel.address.value?.uf)
        spStates.setSelection(stateToSelect)
    }

    private fun sendSignUp() {
        if(validateFields())
            submitData()
    }

    private fun validateFields(): Boolean = (validPersonalData() && validAddressData() && validLoginData())

    private fun validPersonalData(): Boolean {
        etFullName.validate(getString(R.string.nameRequiredMsg)) { s -> s.isNotEmpty() }
        etCPF.validate(getString(R.string.cpfRequiredMsg)) { s -> s.isNotEmpty() }

        return etFullName.error == null && etCPF.error == null
    }

    private fun validAddressData(): Boolean {
        etZipCode.validate(getString(R.string.zipCodeRequiredMsg)) { s -> s.isNotEmpty() }
        etStreetAddress.validate(getString(R.string.streetRequiredMsg)) { s -> s.isNotEmpty() }
        etNumber.validate(getString(R.string.numberRequiredMsg)) { s -> s.isNotEmpty() }
        etNeighborhood.validate(getString(R.string.etNeighborhoodRequired)) { s -> s.isNotEmpty() }
        etCity.validate(getString(R.string.cityRequiredMsg)) { s -> s.isNotEmpty() }

        return etZipCode.error == null && etStreetAddress.error == null && etNumber.error == null
                && etNeighborhood.error == null && etCity.error == null
    }

    private fun validLoginData(): Boolean {

        etEmail.validate(getString(R.string.emailRequiredMsg)) { s -> s.isNotEmpty() }
        etEmail.validate(getString(R.string.emailInvalidMsg)) { s -> s.isValidEmail() }
        etPassword.validate(getString(R.string.passwordRequiredMsg)) { s -> s.isNotEmpty() }
        etConfirmPassword.validate(getString(R.string.passwordRequiredMsg)) { s -> s.isNotEmpty() }
        etConfirmPassword.validate(getString(R.string.passwordNotMatchMsg)) { s -> s.equals(etPassword.text.toString()) }

        return etEmail.error == null && etPassword.error == null && etConfirmPassword.error == null
    }

    private fun submitData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
