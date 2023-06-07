package com.example.myapplication

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var itemList: ArrayList<myData> = ArrayList()
    private var itemList1: ArrayList<ItemsViewModel1> = ArrayList()
    private lateinit var result: String
    private lateinit var btnOpen: Button
    lateinit var recyclerView: RecyclerView
    lateinit var customAdapter: CustomAdapter
    lateinit var date1: TextView
    lateinit var date2: TextView
    lateinit var result1: TextView
    lateinit var period: EditText
    lateinit var btnCheck: Button
    val format = SimpleDateFormat("dd/MM/yyyy")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btnOpen = findViewById(R.id.btnOpen)
        date1 = findViewById(R.id.date1)
        date2 = findViewById(R.id.date2)
        period = findViewById(R.id.period)
        btnCheck = findViewById(R.id.btnOpen)
        result1 = findViewById(R.id.tvresult)

        val materialDateBuilder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("SELECT A DATE")
        val materialDatePicker = materialDateBuilder.build()


        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, itemList)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompletetv)
        autoCompleteTextView.setAdapter(arrayAdapter)
        arrayAdapter.notifyDataSetChanged()





        date1.setOnClickListener {
            materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
            materialDatePicker.addOnPositiveButtonClickListener {
                val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utc.timeInMillis = it as Long
                val format = SimpleDateFormat("dd/MM/yyyy")
                val formatted: String = format.format(utc.time)
                date1.text = formatted
            }
        }

        date2.setOnClickListener {
            materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
            materialDatePicker.addOnPositiveButtonClickListener {
                val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utc.timeInMillis = it as Long
                val format = SimpleDateFormat("dd/MM/yyyy")
                val formatted: String = format.format(utc.time)
                date2.text = formatted
            }
        }



        btnCheck.setOnClickListener {
            checkDateValidity(date1.text.toString(), date2.text.toString(), period.text.toString())
        }

    }


    private fun checkDateValidity(date1: String, date2: String, period: String) {
        if (period.isNotEmpty()) {
            if (date2.isNotEmpty()) {
                //convert string to date
                val dateTwo = SimpleDateFormat("dd/MM/yyyy").parse(date2)
                //compare with s date
                isValidDate(dateTwo, period.toInt())
            } else {
                //compare with f date
                val dateOne = SimpleDateFormat("dd/MM/yyyy").parse(date1)
                isValidDate(dateOne, period.toInt())
            }
        }

    }

    private fun isValidDate(data: Date, period: Int): Boolean {
        val currentDate = Date()
        val cal = Calendar.getInstance()
        cal.time = data
        cal.add(Calendar.MONTH, period)
        result1.text = format.format(cal.time)

        return if (cal.time > currentDate) {
            Toast.makeText(this, "Valid Date", Toast.LENGTH_LONG).show()
            true
        } else {
            Toast.makeText(this, "In Valid Date", Toast.LENGTH_LONG).show()
            false
        }
    }


    @SuppressLint("MissingInflatedId")
    fun showAlertDialogButtonClicked(view: View) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Name")

        // set the custom layout
        val customLayout: View = layoutInflater.inflate(R.layout.custom_layout, null)

        recyclerView = customLayout.findViewById(R.id.rvList)
        // this creates a vertical layout Manager
        recyclerView.layoutManager = LinearLayoutManager(this)
        // This will pass the ArrayList to our Adapter

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        for (i in 1..20) {
            data.add(ItemsViewModel("Item " + i))
        }
        customAdapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerView.adapter = customAdapter

        builder.setView(customLayout)
        // add a button
        builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
            // send data from the AlertDialog to the Activity
            val editText = customLayout.findViewById<EditText>(R.id.editText)
            sendDialogDataToActivity(editText.text.toString())
        }
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    // Do something with the data coming from the AlertDialog
    private fun sendDialogDataToActivity(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
    }

    fun showAlertDialogButtonClicked1(view: View) {
        for (i in 1..20) {
            itemList1.add(ItemsViewModel1(1 + i))
        }


    }


}

data class ItemsViewModel(val text: String) {}

data class ItemsViewModel1(val num: Int) {}

