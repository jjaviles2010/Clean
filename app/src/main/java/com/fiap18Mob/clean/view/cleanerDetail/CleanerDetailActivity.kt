package com.fiap18Mob.clean.view.cleanerDetail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fiap18Mob.clean.R
import kotlinx.android.synthetic.main.activity_cleaner_detail.*
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class CleanerDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleaner_detail)

        btnDatePicker.setOnClickListener { selectDate() }
        btnTimePicker.setOnClickListener { selectTime() }
    }

    fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            etScheduleDate.setText("$dayOfMonth/$monthOfYear/$year")
        }, year, month, day)

        datePickerDialog.show()
    }

    fun selectTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE);

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            etCleaningTime.setText("$hourOfDay:$minute")
        }, hour, minute, false)

        timePickerDialog.show()
    }
}
