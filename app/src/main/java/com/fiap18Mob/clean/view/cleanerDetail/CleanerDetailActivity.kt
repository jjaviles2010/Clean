package com.fiap18Mob.clean.view.cleanerDetail

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.fiap18Mob.clean.BaseActivity
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.model.CleaningService
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.utils.PermissionUtils
import com.fiap18Mob.clean.utils.Status
import com.fiap18mob.mylib.CustomToast
import kotlinx.android.synthetic.main.activity_cleaner_detail.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class CleanerDetailActivity : BaseActivity() {

    val cleanerDetailViewModel: CleanerDetailViewModel by viewModel()
    private val cleaningService: CleaningService by inject()
    val customToast: CustomToast by inject()

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
        tvFullAddress.text =
            "${user.street} ${user.number}, ${user.complement}, ${user.neighborhood}, ${user.city}, ${user.uf}"
    }

    private fun setupButtonEvents() {
        btnServDatePicker.setOnClickListener { selectDate() }
        btnTimePicker.setOnClickListener { selectTime() }
        btnUpdateService.setOnClickListener { scheduleService() }
        btnCallCleaner.setOnClickListener { startCallProcess() }
        btnShareCleanerInfo.setOnClickListener { shareCleanerInfo() }
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
                customToast.showToast(this, it, CustomToast.ERROR)
        })

        cleanerDetailViewModel.cleaningServiceScheduled.observe(this, Observer {
            if (it == true) {
                customToast.showToast(this, getString(R.string.cleaningServiceScheduledMsg), CustomToast.SUCCESS)
                finish()
            }
        })
    }

    fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                etServScheduleDate.setText("$dayOfMonth/${monthOfYear + 1}/$year")
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun selectTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE);

        val timePickerDialog =
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                etCleanServiceTime.setText(getTimeToShow(hourOfDay, minute))
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
        cleaningService.cleaningStatus = Status.ServiceStatus.OPENED.status
        cleaningService.scheduledTime = getScheduleDate()
    }

    private fun getScheduleDate(): Long {
        val dateFields = etServScheduleDate.text.split("/")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.parse(
            "${dateFields[2]}-${dateFields[1]}-${dateFields[0]} ${populateTime(
                etCleanServiceTime.text.toString()
            )}:00"
        ).time

    }

    private fun getTimeToShow(hourOfDay: Int, minute: Int): String {
        var hourText: String
        var minuteText: String

        hourText = if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
        minuteText = if (minute < 10) "0$minute" else "$minute"

        return "$hourText:$minuteText"
    }

    private fun populateTime(timeInText: String): String {
        val hour = timeInText.split(':')[0].toInt()
        val minute = timeInText.split(':')[1].toInt()

        return "$hour:$minute"
    }


    private fun startCallProcess() {
        checkPermissions()
    }

    private fun checkPermissions() {
        if (PermissionUtils.validate(
                this, 0,
                Manifest.permission.CALL_PHONE
            )
        ) {
            callCleaner()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (result in grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                alertAndFinish()
                return
            } else {
                callCleaner()
            }
        }
    }

    private fun alertAndFinish() {
        run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name)
                .setMessage("Para utilizar este aplicativo, você precisa aceitar as permissões.")
            // Add the buttons
            builder.setPositiveButton("OK") { dialog, id -> finish() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun callCleaner() {
        val callIntent =
            Intent(Intent.ACTION_CALL, Uri.parse("tel:${user.phoneNumber}")).apply {
                startActivity(this)
            }
    }

    private fun shareCleanerInfo() {
        val shareInfoIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${user.nome}, tel:${user.phoneNumber}, val:${user.hourValue}")
            type = "text/plain"
        }

        if (shareInfoIntent.resolveActivity(packageManager) != null) {
            startActivity(shareInfoIntent)
        }
    }
}
