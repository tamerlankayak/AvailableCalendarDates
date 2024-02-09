package com.example.myapplication

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showCalendar()

        binding.layout.setOnClickListener {
            showCalendar()
        }
    }


    private fun showCalendar() {
        val specialDates = listOf(
            CalendarDay(2024, Calendar.JANUARY, 8),
            CalendarDay(2024, Calendar.FEBRUARY, 7),
            CalendarDay(2024, Calendar.FEBRUARY, 14),
            CalendarDay(2024, Calendar.FEBRUARY, 20),
            CalendarDay(2024, Calendar.MARCH, 20),
            CalendarDay(2024, Calendar.MARCH, 3),
            CalendarDay(2024, Calendar.MARCH, 4),
            CalendarDay(2024, Calendar.MARCH, 5),
            CalendarDay(2024, Calendar.MARCH, 7),
            CalendarDay(2024, Calendar.MARCH, 11)
            // Add more special dates as needed
        )
        setLocale(this, "az")
        val materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker().setTheme(R.style.Widget_AppTheme_MaterialDatePicker)
        val constraintsBuilder = CalendarConstraints.Builder()

        val currentDate = Calendar.getInstance()

        // Set the minimum date to the current date to disable selecting past dates
        constraintsBuilder.setStart(currentDate.timeInMillis)

        materialDatePickerBuilder.setCalendarConstraints(constraintsBuilder.build())
        constraintsBuilder.setValidator(SpecialDateValidator(specialDates))

        materialDatePickerBuilder.setCalendarConstraints(constraintsBuilder.build())

        var materialDatePicker = materialDatePickerBuilder.build()
        // Hide the toggle edit icon (workaround)

        materialDatePicker.addOnPositiveButtonClickListener(
            MaterialPickerOnPositiveButtonClickListener { selection ->
                // Handle date selection
                Toast.makeText(this, selection.toString(), Toast.LENGTH_SHORT).show()
            })

        materialDatePicker.show(supportFragmentManager, "MaterialDatePicker")
    }

    private class SpecialDateValidator(private val specialDates: List<CalendarDay>) :
        CalendarConstraints.DateValidator {

        constructor(parcel: Parcel) : this(emptyList())

        override fun isValid(date: Long): Boolean {
            // Check if the selected date is in the list of special dates
            val cal = Calendar.getInstance()
            cal.timeInMillis = date

            val selectedDate = CalendarDay(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            return specialDates.contains(selectedDate)
        }


        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {

        }

        companion object CREATOR : Parcelable.Creator<SpecialDateValidator> {
            override fun createFromParcel(parcel: Parcel): SpecialDateValidator {
                return SpecialDateValidator(parcel)
            }

            override fun newArray(size: Int): Array<SpecialDateValidator?> {
                return arrayOfNulls(size)
            }
        }
    }

    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    data class CalendarDay(val year: Int, val month: Int, val day: Int)



}



