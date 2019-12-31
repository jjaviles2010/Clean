package com.fiap18Mob.clean.view.serviceDetail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.fiap18Mob.clean.BaseActivity
import androidx.lifecycle.Observer
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.model.CleaningService
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.utils.toDateTime
import kotlinx.android.synthetic.main.activity_service_detail.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.text.SimpleDateFormat


class ServiceDetailActivity : BaseActivity() {

    val serviceDetailViewModel : ServiceDetailViewModel by viewModel()
    lateinit var cleaningService : CleaningService
    lateinit var cleanerInfo : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)

        populateSpinner()
        setValues()
        setupButtonEvents()
        configureObservers()
        populateCleanerInfo()
    }

    private fun setValues() {
        cleaningService = intent.getParcelableExtra("SERVICE")
        tvProfessionalName.text = cleaningService.cleanerName
        etServScheduleDate.setText(cleaningService.scheduledTime.toDateTime().split(" ")[0])
        etCleanServiceTime.setText(cleaningService.scheduledTime.toDateTime().split(" ")[1])
        val statusToSelect = resources.getStringArray(R.array.statesList).indexOf(cleaningService.cleaningStatus)
        spServiceStatus.setSelection(statusToSelect)
    }

    private fun populateSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.statusList,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spServiceStatus.adapter = adapter
    }

    private fun setupButtonEvents() {
        btnServDatePicker.setOnClickListener { selectDate() }
        btnTimePicker.setOnClickListener { selectTime() }
        btnUpdateService.setOnClickListener { updateService() }
    }

    private fun configureObservers() {
        serviceDetailViewModel.isLoading.observe(this, Observer {
            if (it == true) {
                containerLoading.visibility = View.VISIBLE
            } else {
                containerLoading.visibility = View.GONE
            }
        })

        serviceDetailViewModel.messageError.observe(this, Observer {
            if (it != "")
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        serviceDetailViewModel.cleanerInfo.observe(this, Observer {
            cleanerInfo = it
            tvCleanerPhoneNum.text = cleanerInfo.phoneNumber
            tvCleanerHourVal.text = cleanerInfo.hourValue.toString()

        })

        serviceDetailViewModel.cleaningServiceUpdated.observe(this, Observer {
            if (it == true) {
                Toast.makeText(this, getString(R.string.msgCleaningServUpdated), Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun populateCleanerInfo() {
        serviceDetailViewModel.isLoading.value = true
        serviceDetailViewModel.getCleanerInfo(cleaningService.cleanerCPF)
    }

    fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            etServScheduleDate.setText("$dayOfMonth/$monthOfYear/$year")
        }, year, month, day)

        datePickerDialog.show()
    }

    fun selectTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE);

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            etCleanServiceTime.setText("$hourOfDay:$minute")
        }, hour, minute, false)

        timePickerDialog.show()
    }


    fun updateService() {
        populateServiceInfo()
        serviceDetailViewModel.isLoading.value = true
        serviceDetailViewModel.updateCleaningService(cleaningService)
    }

    private fun populateServiceInfo() {
        cleaningService.cleaningStatus = spServiceStatus.selectedItem.toString()
        cleaningService.scheduledTime = getScheduleDate()
    }

    private fun getScheduleDate() : Long {
        val dateFields = etServScheduleDate.text.split("/")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.parse("${dateFields[2]}-${dateFields[1]}-${dateFields[0]} ${etCleanServiceTime.text}:00").time
    }

}

