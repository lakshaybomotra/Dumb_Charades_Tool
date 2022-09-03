package com.lbdev.dumbcharadestool

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.lbdev.dumbcharadestool.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var _binding: ActivityMainBinding
    private var d: Drawable? = null
    private var _startTime: Long = 0
    private var pauseState: Boolean = false
    private var doubleBackToExitPressedOnce = false

    @SuppressLint("UseCompatLoadingForDrawables", "StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        if (isDarkMode(this)) {
            _binding.chronometer.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )

        } else {
            _binding.chronometer.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.black,
                    null
                )
            )
            _binding.clock.setColorFilter(ResourcesCompat.getColor(resources, R.color.black, null))
        }



        _binding.chronometer.typeface = ResourcesCompat.getFont(this, R.font.digital7)
        _binding.btnStop.alpha = 0.0f

        _binding.btnStart.typeface = ResourcesCompat.getFont(this, R.font.mmedium)
        _binding.btnStop.typeface = ResourcesCompat.getFont(this, R.font.mmedium)

        _binding.btnStart.setOnClickListener {
            if (!pauseState) {
                _binding.btnStop.animate().alpha(1f).duration = 300
                _binding.btnStart.alpha = 1.0f
                _binding.btnStart.isEnabled = true
                _binding.btnStop.isEnabled = true
                _binding.chronometer.base = SystemClock.elapsedRealtime()
                _binding.chronometer.start()
                d = _binding.clock.drawable
                if (d is Animatable) {
                    (d as Animatable).start()
                }
                _binding.btnStart.background = getDrawable(R.drawable.bgbtnred)
                _binding.btnStart.text = getString(R.string.pause)
                pauseState = true
            } else if (_startTime == 0L) {
                _startTime = SystemClock.elapsedRealtime() - _binding.chronometer.base
                _binding.chronometer.stop()
                _binding.btnStart.text = getString(R.string.resume)
                if (d is Animatable) {
                    (d as Animatable).stop()
                }
            } else {
                _binding.chronometer.base = SystemClock.elapsedRealtime() - _startTime
                _binding.chronometer.start()
                _startTime = 0L
                _binding.btnStart.text = getString(R.string.pause)
                if (d is Animatable) {
                    (d as Animatable).start()
                }
            }

        }



        _binding.btnStop.setOnClickListener {
            _binding.btnStop.alpha = 0.0f
            _binding.btnStop.isEnabled = false
            _binding.chronometer.stop()
            _binding.chronometer.text = getString(R.string.zero_timer)
            pauseState = false
            if (d is Animatable) {
                (d as Animatable).stop()
            }
            pauseState = false
            _binding.btnStart.background = getDrawable(R.drawable.bgbtngreen)
            _binding.btnStart.text = getString(R.string.start)
        }


    }

    private fun isDarkMode(context: Context): Boolean {
        val darkModeFlag =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}