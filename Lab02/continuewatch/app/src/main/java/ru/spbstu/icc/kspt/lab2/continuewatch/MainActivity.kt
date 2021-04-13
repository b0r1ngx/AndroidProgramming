package ru.spbstu.icc.kspt.lab2.continuewatch

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var RESUMED_STATE = true
    private lateinit var sharedSeconds: SharedPreferences

    private var backgroundThread = Thread {
        while (true) {
            while (RESUMED_STATE) {
                Thread.sleep(1000)
                textSecondsElapsed.post {
                    secondsElapsed++
                    textSecondsElapsed.text = getString(R.string.SE, secondsElapsed)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedSeconds = getDefaultSharedPreferences(this)
        secondsElapsed = sharedSeconds.getInt(SECONDS_KEY, secondsElapsed)
        textSecondsElapsed.text = getString(R.string.SE, secondsElapsed)
        backgroundThread.start()
    }


    override fun onResume() {
        super.onResume()
        RESUMED_STATE = true
    }

    override fun onPause() {
        super.onPause()
        RESUMED_STATE = false
    }

    override fun onStop() {
        super.onStop()
        with (sharedSeconds.edit()) {
            putInt(SECONDS_KEY, secondsElapsed)
            apply()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SECONDS_KEY, secondsElapsed)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.run {
            secondsElapsed = getInt(SECONDS_KEY)
        }
    }

    fun onClick(view: View) {
        secondsElapsed = 0
        Toast.makeText(applicationContext, getString(R.string.reset_toast), Toast.LENGTH_SHORT).show()
    }

    companion object {
        internal var secondsElapsed: Int = 0
        internal const val SECONDS_KEY = "sec"
    }
}
