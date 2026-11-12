package com.example.secondalarm

import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : Activity() {
    private val selectedDate = Calendar.getInstance()
    private lateinit var dateLabel: TextView
    private lateinit var hourInput: EditText
    private lateinit var minuteInput: EditText
    private lateinit var secondInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 36, 28, 24)
        }

        fun label(value: String) {
            layout.addView(TextView(this).apply {
                text = value
                textSize = 17f
                setPadding(0, 14, 0, 6)
            })
        }

        layout.addView(TextView(this).apply {
            text = "SECOND ALARM"
            textSize = 26f
            setPadding(0, 0, 0, 18)
        })

        label("Choose alarm date")
        dateLabel = TextView(this).apply { textSize = 18f }
        layout.addView(dateLabel)
        layout.addView(Button(this).apply {
            text = "SELECT DATE"
            setOnClickListener { showDatePicker() }
        })
        updateDateLabel()

        label("Enter alarm time (24-hour format)")
        val timeRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        hourInput = numberField("HH", 2)
        minuteInput = numberField("MM", 2)
        secondInput = numberField("SS", 2)
        timeRow.addView(hourInput, weightedParams())
        timeRow.addView(TextView(this).apply { text = " : "; textSize = 24f })
        timeRow.addView(minuteInput, weightedParams())
        timeRow.addView(TextView(this).apply { text = " : "; textSize = 24f })
        timeRow.addView(secondInput, weightedParams())
        layout.addView(timeRow)

        layout.addView(Button(this).apply {
            text = "SET ONE-TIME ALARM"
            setOnClickListener { scheduleAlarm() }
        })
        layout.addView(TextView(this).apply {
            text = "Example: 06 : 45 : 32\nKeep the phone powered on. Allow this app to run without battery restrictions for best reliability."
            textSize = 14f
            setPadding(0, 20, 0, 0)
        })

        setContentView(layout)
    }

    private fun numberField(hintText: String, max: Int): EditText =
        EditText(this).apply {
            hint = hintText
            textSize = 22f
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            filters = arrayOf(android.text.InputFilter.LengthFilter(max))
            setSingleLine(true)
        }

    private fun weightedParams() = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                selectedDate.set(year, month, day)
                updateDateLabel()
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateLabel() {
        dateLabel.text = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(selectedDate.time)
    }

    private fun scheduleAlarm() {
        val h = hourInput.text.toString().toIntOrNull()
        val m = minuteInput.text.toString().toIntOrNull()
        val s = secondInput.text.toString().toIntOrNull()

        if (h == null || m == null || s == null || h !in 0..23 || m !in 0..59 || s !in 0..59) {
            Toast.makeText(this, "Enter a valid time: HH 0-23, MM 0-59, SS 0-59", Toast.LENGTH_LONG).show()
            return
        }

        val alarmTime = selectedDate.clone() as Calendar
        alarmTime.set(Calendar.HOUR_OF_DAY, h)
        alarmTime.set(Calendar.MINUTE, m)
        alarmTime.set(Calendar.SECOND, s)
        alarmTime.set(Calendar.MILLISECOND, 0)

        if (alarmTime.timeInMillis <= System.currentTimeMillis()) {
            Toast.makeText(this, "Choose a future date and time.", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(this, AlarmReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            this, 1001, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime.timeInMillis, pending)

        val display = SimpleDateFormat("dd MMM yyyy 'at' HH:mm:ss", Locale.getDefault()).format(alarmTime.time)
        Toast.makeText(this, "One-time alarm set for $display", Toast.LENGTH_LONG).show()
    }
}
