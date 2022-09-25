package com.lbdev.dumbcharadestool

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lbdev.dumbcharadestool.databinding.ActivityMovieGeneratorBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit

class MovieGenerator : AppCompatActivity() {
    lateinit var _binding: ActivityMovieGeneratorBinding
    lateinit var toggle: ActionBarDrawerToggle
    private var moviesTitle: ArrayList<String> = ArrayList<String>()
    lateinit var fabNext: FloatingActionButton
    lateinit var fabPrev: FloatingActionButton
    private var doubleBackToExitPressedOnce = false
    private var movieShownList: ArrayList<String> = ArrayList<String>()
    private var movieRealSize: Int = 0
    var nextStartIndex: Int = 0
    var nextLastIndex: Int = 9
    var prevStartIndex: Int = 0
    var prevLastIndex: Int = 9

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMovieGeneratorBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        moviesTitle = intent.getStringArrayListExtra("movies") as ArrayList<String>
        Log.d("Total titles", "onCreate:  ${moviesTitle.size}")

        movieRealSize = moviesTitle.size
        val drawerLayout = _binding.drawerLayout
        val navView = _binding.navView

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fabNext = _binding.btnGenerate
        fabPrev = _binding.btnPrev

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_stopwatch -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    MovieGenerator().finish()
                }
                R.id.nav_movieGenerator -> {
                    val intent = Intent(this, ShowMovies::class.java)
                    startActivity(intent)
                    MovieGenerator().finish()
                }
                R.id.nav_exit -> finish()
            }
            true
        }

        if (isDarkMode(this)) {
            fabNext.backgroundTintList =
                ResourcesCompat.getColorStateList(resources, R.color.white, null)
            fabNext.imageTintList =
                ResourcesCompat.getColorStateList(resources, R.color.black, null)
            fabPrev.backgroundTintList =
                ResourcesCompat.getColorStateList(resources, R.color.white, null)
            fabPrev.imageTintList =
                ResourcesCompat.getColorStateList(resources, R.color.black, null)

        } else {
            fabNext.backgroundTintList =
                ResourcesCompat.getColorStateList(resources, R.color.black, null)
            fabNext.imageTintList =
                ResourcesCompat.getColorStateList(resources, R.color.white, null)
            fabPrev.backgroundTintList =
                ResourcesCompat.getColorStateList(resources, R.color.black, null)
            fabPrev.imageTintList =
                ResourcesCompat.getColorStateList(resources, R.color.white, null)
        }

        showNextMovieTitles()

        _binding.btnGenerate.setOnClickListener {
            showNextMovieTitles()
            prevStartIndex = nextStartIndex-10
            prevLastIndex = nextLastIndex-10
        }

        _binding.btnPrev.setOnClickListener {
            showPrevMovieTitles()
            nextStartIndex = prevStartIndex+10
            nextLastIndex = prevLastIndex+10
        }

    }

    private fun showNextMovieTitles() {
        val recyclerview = findViewById<RecyclerView>(R.id.rvMovies)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = mutableListOf<ItemsViewModel>()

        if (movieShownList.size == movieRealSize) {
            movieShownList.clear()
            movieRealSize = moviesTitle.size
            Toast.makeText(
                this,
                "All movies have been shown moving back to first",
                Toast.LENGTH_SHORT
            ).show()
            nextStartIndex = 0
            nextLastIndex = 9
            prevStartIndex = 0
            prevLastIndex = 9
        }

        if (moviesTitle.size > 0) {
            for (i in nextStartIndex..nextLastIndex) {
                if (i < movieRealSize) {
                    data.add(ItemsViewModel(i + 1, moviesTitle[i]))
                    movieShownList.add(moviesTitle[i])
                }
            }
        } else {
            Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show()
        }

        nextStartIndex += 10
        nextLastIndex += 10

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
    }

    private fun showPrevMovieTitles() {
        if (prevStartIndex >= 0) {
            val recyclerview = findViewById<RecyclerView>(R.id.rvMovies)

            // this creates a vertical layout Manager
            recyclerview.layoutManager = LinearLayoutManager(this)

            // ArrayList of class ItemsViewModel
            val data = mutableListOf<ItemsViewModel>()

            for (i in prevStartIndex..prevLastIndex) {
                data.add(ItemsViewModel(i + 1, moviesTitle[i]))
                movieShownList.remove(moviesTitle[i])
            }

            prevStartIndex -= 10
            prevLastIndex -= 10


            // This will pass the ArrayList to our Adapter
            val adapter = CustomAdapter(data)

            // Setting the Adapter with the recyclerview
            recyclerview.adapter = adapter
        } else {
            Toast.makeText(this, "This is first Page", Toast.LENGTH_SHORT).show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}


