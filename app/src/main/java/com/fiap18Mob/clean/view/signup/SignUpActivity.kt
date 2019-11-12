package com.fiap18Mob.clean.view.signup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Visibility
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.repository.UserRepository
import com.fiap18Mob.clean.utils.Mask
import com.fiap18Mob.clean.utils.isValidEmail
import com.fiap18Mob.clean.utils.validate
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity() {

    val signUpViewModel: SignUpViewModel by viewModel()
    private val user: User by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        getExtras()
        populateSpinner()
        configureMasks()
        configureListeners()
        configureGeneralObservers()
        configureUserObservers()

        btnSendSignUp.setOnClickListener {
            sendSignUp()
        }
    }


    private fun getExtras() {
        if (intent.extras?.getString("USER_TYPE") == "CLIENT") {
            user.profile = "CLIENT"
            etHourValue.visibility = View.GONE
        } else {
            user.profile = "CLEANER"
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


    private fun configureMasks() {
        etCPF.addTextChangedListener(Mask.mask("###.###.###-##", etCPF))
        etZipCode.addTextChangedListener(Mask.mask("#####-###", etZipCode))
        etPhone.addTextChangedListener(Mask.mask("(##) #####-####", etPhone))
    }


    private fun configureListeners() {
        etZipCode.setOnFocusChangeListener { view, b ->
            searchAddress(
                b,
                etZipCode.text.toString()
            )
        }
        etCPF.setOnFocusChangeListener { view, b -> searchUserLocalData(b, etCPF.text.toString()) }
    }


    private fun searchAddress(b: Boolean, zipCode: String) {
        if(!b && zipCode.length == 9) {
            signUpViewModel.getAddress(Mask.replaceChars(zipCode))
        }
    }


    private fun searchUserLocalData(b: Boolean, cpf: String) {
        if(!b && cpf.length == 14) {
            signUpViewModel.getUserLocally(Mask.replaceChars(cpf))
        }
    }


    private fun configureGeneralObservers() {
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
    }


    private fun configureUserObservers() {
        signUpViewModel.user.observe(this, Observer {
            if (it != null) {
                etFullName.setText(it.nome)
                etPhone.setText(it.phoneNumber)
                etZipCode.setText(it.zipCode)
                etStreetAddress.setText(it.street)
                etNumber.setText(it.number.toString())
                etAddressComp.setText(it.complement)
                etNeighborhood.setText(it.neighborhood)
                etCity.setText(it.city)
                val stateToSelect = resources.getStringArray(R.array.statesList).indexOf(it.uf)
                spStates.setSelection(stateToSelect)
                etEmail.setText(it.email)
            }
        })

        signUpViewModel.isUserSignUp.observe(this, Observer {
            if (it == true) {
                submitData()
            } else {
                Toast.makeText(this, getString(R.string.errorCreatingUserMsg), Toast.LENGTH_LONG).show()
            }
        })

        signUpViewModel.isUserCreated.observe(this, Observer {
            if (it == true) {
                val resultIntent = Intent()
                resultIntent.putExtra("email", user.email)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.errorCreatingUserMsg), Toast.LENGTH_LONG)
                    .show()
            }
        })
    }


    private fun showAddressInfo() {
        etStreetAddress.setText(signUpViewModel.address.value?.logradouro)
        etNeighborhood.setText(signUpViewModel.address.value?.bairro)
        etCity.setText(signUpViewModel.address.value?.localidade)
        val stateToSelect = resources.getStringArray(R.array.statesList).indexOf(signUpViewModel.address.value?.uf)
        spStates.setSelection(stateToSelect)
    }


    private fun sendSignUp() {
        if(validateFields()) {
            populateUserData()
            signUpViewModel.isLoading.value = true
            signUpViewModel.signUpUser(user, etPassword.text.toString())
        }
    }


    private fun validateFields(): Boolean = (validPersonalData() && validAddressData() && validLoginData())


    private fun validPersonalData(): Boolean {
        etFullName.validate(getString(R.string.nameRequiredMsg)) { s -> s.isNotEmpty() }
        etCPF.validate(getString(R.string.cpfRequiredMsg)) { s -> s.isNotEmpty() }
        etCPF.validate(getString(R.string.etCPFInvalidMsg)) { s -> s.length > 13 }

        return etFullName.error == null && etCPF.error == null
    }


    private fun validAddressData(): Boolean {
        etZipCode.validate(getString(R.string.zipCodeRequiredMsg)) { s -> s.isNotEmpty() }
        etZipCode.validate(getString(R.string.etZipCodeInvalidMsg)) { s -> s.length > 8 }
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
        signUpViewModel.insertUserRemote(user)
    }


    override fun onPause() {
        super.onPause()
        populateUserData()
        saveUserDataLocally()
    }


    private fun populateUserData() {
        user.cpf = Mask.replaceChars(etCPF.text.toString())
        user.nome = etFullName.text.toString()
        user.phoneNumber = Mask.replaceChars(etPhone.text.toString())
        user.zipCode = Mask.replaceChars(etZipCode.text.toString())
        user.street = etStreetAddress.text.toString()
        user.number = etNumber.text.toString().toIntOrNull() ?: 0
        user.complement = etAddressComp.text.toString()
        user.neighborhood = etNeighborhood.text.toString()
        user.city = etCity.text.toString()
        user.uf = spStates.selectedItem.toString()
        user.email = etEmail.text.toString()
        user.hourValue = etHourValue.text.toString().toDoubleOrNull() ?: 0.0
    }


    fun saveUserDataLocally() {
        signUpViewModel.insertUserLocally(user)
    }

}
