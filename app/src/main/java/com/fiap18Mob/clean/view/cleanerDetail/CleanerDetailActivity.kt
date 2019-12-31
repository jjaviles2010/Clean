package com.fiap18Mob.clean.view.cleanerDetail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fiap18Mob.clean.BaseActivity
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.model.CleaningService
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.utils.ServiceStatus
import kotlinx.android.synthetic.main.activity_cleaner_detail.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class CleanerDetailActivity : BaseActivity() {

    val cleanerDetailViewModel : CleanerDetailViewModel by viewModel()
    private val cleaningService : CleaningService by inject()

    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleaner_detail)

        setValues()

        setupButtonEvents()
        configureObservers()
    }

    private fun setValues() {
        user = intent.getParcelableExtra("USER")
        tvCleanerName.text = user.nome
        tvCleanerPhoneNum.text = user.phoneNumber
        tvCleanerHourVal.text = user.hourValue.toString()
        tvFullAddress.text = "${user.street} ${user.number}, ${user.complement}, ${user.neighborhood}, ${user.city}, ${user.uf}"
    }

    private fun setupButtonEvents() {
        btnServDatePicker.setOnClickListener { selectDate() }
        btnTimePicker.setOnClickListener { selectTime() }
        btnUpdateService.setOnClickListener { scheduleService() }
    }

    private fun configureObservers() {
        cleanerDetailViewModel.isLoading.observe(this, Observer {
            if (it == true) {
                containerLoading.visibility = View.VISIBLE
            } else {
                containerLoading.visibility = View.GONE
            }
        })

        cleanerDetailViewModel.messageError.observe(this, Observer {
            if (it != "")
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        cleanerDetailViewModel.cleaningServiceScheduled.observe(this, Observer {
            if (it == true) {
                Toast.makeText(this, getString(R.string.cleaningServiceScheduledMsg), Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            etServScheduleDate.setText("$dayOfMonth/${monthOfYear+1}/$year")
        }, year, month, day)

        datePickerDialog.show()
    }

    fun selectTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE);

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            etCleanServiceTime.setText(getTimeToShow(hourOfDay,minute))
        }, hour, minute, false)

        timePickerDialog.show()
    }

    fun scheduleService() {
        populateServiceInfo()
        cleanerDetailViewModel.isLoading.value = true
        cleanerDetailViewModel.insertCleaningService(cleaningService)
    }

    private fun populateServiceInfo() {
        cleaningService.cleanerName = user.nome
        cleaningService.cleanerCPF = user.cpf
        cleaningService.cleaningStatus = ServiceStatus.OPENED.status
        cleaningService.scheduledTime = getScheduleDate()
    }

    private fun getScheduleDate() : Long {
        val dateFields = etServScheduleDate.text.split("/")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.parse("${dateFields[2]}-${dateFields[1]}-${dateFields[0]} ${populateTime(etCleanServiceTime.text.toString())}:00").time

    }

    private fun getTimeToShow(hourOfDay: Int, minute: Int) : String {
        var hourText: String
        var minuteText: String

        hourText = if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
        minuteText = if (minute < 10) "0$minute" else "$minute"

        return "$hourText:$minuteText"
    }

    private fun populateTime(timeInText: String) : String {
        val hour = timeInText.split(':')[0].toInt()
        val minute = timeInText.split(':')[1].toInt()

        return "$hour:$minute"
    }
}
