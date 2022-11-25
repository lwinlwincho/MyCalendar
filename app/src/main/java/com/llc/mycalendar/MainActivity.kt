package com.llc.mycalendar

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.llc.mycalendar.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityMainBinding

    private val month = arrayOf<String>("1", "2", "3","4","5","6","7","8","9")

    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBar.myToolbar)
       /* binding.myToolbar.setNavigationOnClickListener{
        }*/

        binding.spinner.onItemSelectedListener = this
        //  using the custom array and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, month)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        //  using the string array and a default spinner layout
        /*ArrayAdapter.createFromResource(
            this,
            R.array.months_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }*/

        // on below line we are creating a variable to which we have to set our spinner item selected.
        val selection = "January"
        // on below line we are getting the position of the item by the item name in our adapter.
        val spinnerPosition: Int = adapter.getPosition(selection)
        // on below line we are setting selection for our spinner to spinner position.
        binding.spinner.setSelection(spinnerPosition)


        // Add Listener in calendar
        binding.calendarView.setOnDateChangeListener(
            CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
                // In this Listener we are getting values such as year, month and day of month
                // on below line we are creating a variable in which we are adding all the variables in it.
                val date = (dayOfMonth.toString() + "-" + (month + 1) + "-" + year)
                // set this date in TextView for Display
                binding.tvDateView.text = date
            })


        binding.btnSpeech.isEnabled = false
        // TextToSpeech(Context: this, OnInitListener: this)
        textToSpeech = TextToSpeech(this, this)
        binding.btnSpeech.setOnClickListener { speakOut() }
    }

    //for spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // on below line we are displaying toast message for selected item.
        Toast.makeText(this, "" + month[position] + " Selected..", Toast.LENGTH_SHORT).show()
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    //for text to speech listener
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            val result = textToSpeech!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Log.e("TTS","The Language not supported!")
                Toast.makeText(this,"Wrong",Toast.LENGTH_LONG)
               // showToast(this,"The language not supported!",1)
            } else {
                binding.btnSpeech!!.isEnabled = true
            }
        }
    }

    private fun speakOut() {
        val text = binding.etSpeech!!.text.toString()
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        super.onDestroy()
    }



    private fun showToast(
        context: Context = applicationContext,
        message: String,
        duration: Int = Toast.LENGTH_LONG
    ) {
        Toast.makeText(context, message, duration).show()
    }
}