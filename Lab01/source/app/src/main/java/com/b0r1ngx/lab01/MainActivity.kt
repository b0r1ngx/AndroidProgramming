package com.b0r1ngx.lab01

import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.util.Linkify
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Context

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "onCreate() called")


        val textView = findViewById<TextView>(R.id.TextViewLicense)
        //Modeling Germany User
        val dePLMN = 26233
    }

    fun onClick(view: View) {
        val text = findViewById<EditText>(R.id.EditText6)
        val checkBox = findViewById<CheckBox>(R.id.CheckBox3)

        if (!checkBox.isChecked)
            Toast.makeText(
                applicationContext,
                "Походу Вы Робот \uD83E\uDD16...",
                Toast.LENGTH_SHORT
            ).show()
        else
            if (text.text.isEmpty())
                Toast.makeText(
                    applicationContext,
                    "Напишите хоть что-нибудь <@:[",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                Toast.makeText(applicationContext, "Будет сделано!", Toast.LENGTH_SHORT).show()
                text.setText("")
                checkBox.isChecked = false
            }
    }

    private fun defineCountry() {
        val manager = 1
    }

    private fun setTextAndHyperlinkToXmlId(textView: TextView, country: String = "") {
        textView.text = ""
        Linkify.addLinks(textView, Linkify.ALL)
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "OnResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "OnPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState() called")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState() called")
    }

    companion object {
        internal const val TAG = "Activity-lifecycle"
        internal const val linkLicenseRU = "http://government.ru/"
        internal const val linkLicenseDE = "https://www.bundesregierung.de"
    }
}