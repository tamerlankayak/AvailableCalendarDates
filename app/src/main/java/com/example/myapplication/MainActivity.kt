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
import java.text.SimpleDateFormat
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
            "2024-01-08",
            "2024-02-07",
            "2024-02-14",
            "2024-02-20",
            "2024-03-20",
            "2024-03-03",
            "2024-03-04",
            "2024-03-05",
            "2024-03-07",
            "2024-07-12",
            "2024-03-11"
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

        materialDatePicker.addOnPositiveButtonClickListener(
            MaterialPickerOnPositiveButtonClickListener { selection ->
                // Convert the selected date to a readable format
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selection

                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = simpleDateFormat.format(calendar.time)

                // Handle date selection
                Toast.makeText(this, "Selected Date: $formattedDate", Toast.LENGTH_SHORT).show()
            })


        materialDatePicker.show(supportFragmentManager, "MaterialDatePicker")
    }

    private class SpecialDateValidator(private val specialDates: List<String>) :
        CalendarConstraints.DateValidator {

        constructor(parcel: Parcel) : this(emptyList())

        override fun isValid(date: Long): Boolean {
            // Check if the selected date is in the list of special dates
            val cal = Calendar.getInstance()
            cal.timeInMillis = date

            val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

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


}



