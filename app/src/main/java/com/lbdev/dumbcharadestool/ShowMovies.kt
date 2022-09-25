package com.lbdev.dumbcharadestool

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.lbdev.dumbcharadestool.databinding.ActivityShowMoviesBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShowMovies : AppCompatActivity(), DatePickerDialog.OnDateSetListener{
    lateinit var binding: ActivityShowMoviesBinding
    lateinit var toggle: ActionBarDrawerToggle
    var titles : String? = null
    var mov : ArrayList<String> = ArrayList()
    private var doubleBackToExitPressedOnce = false
    var originLang : String? = null
    var allDone = false
    var years : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout = binding.drawerLayout
        val navView = binding.navView

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val spinner: Spinner = findViewById(R.id.spnLanguage)
        binding.progressBar.visibility = View.GONE
        val spinner2: Spinner = findViewById(R.id.spnYear)
        getYears()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_stopwatch -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    ShowMovies().finish()
                }
                R.id.nav_movieGenerator -> drawerLayout.closeDrawers()
                R.id.nav_exit -> finish()
            }
            true
        }

        if (isDarkMode(this))
        {
            binding.imgYear.imageTintList = ResourcesCompat.getColorStateList(resources, R.color.white, null)
            binding.imgLanguage.imageTintList = ResourcesCompat.getColorStateList(resources, R.color.white, null)
        }
        else
        {
            binding.imgYear.imageTintList = ResourcesCompat.getColorStateList(resources, R.color.black, null)
            binding.imgLanguage.imageTintList = ResourcesCompat.getColorStateList(resources, R.color.black, null)
        }

        binding.checkBox.setOnClickListener {
            if (binding.checkBox.isChecked) {
                binding.tvSelectYear.visibility = View.GONE
                binding.rlyear.visibility = View.GONE
                Toast.makeText(this, "It will take more time to load", Toast.LENGTH_LONG).show()
            } else {
                binding.tvSelectYear.visibility = View.VISIBLE
                binding.rlyear.visibility = View.VISIBLE
            }
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years)

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = arrayAdapter


        binding.btnGo.setOnClickListener {
            closeKeyBoard()
            binding.progressBar.visibility = View.VISIBLE
            var year: String? = null
            year = if (binding.checkBox.isChecked) {
                "17900"
            } else {
                spinner2.selectedItem.toString()
            }
            val lang = spinner.selectedItem.toString()
            do {
                selectLangCode(lang)
            } while (originLang == null)
            getMovieTitles(year, originLang!!)
            checkProgress()
        }

    }

    private fun getYears() {
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        for (i in 1940..currentYear) {
            years.add(i.toString())
        }
    }

    private fun checkProgress() {

        if (allDone) {
            binding.progressBar.visibility =View.GONE
            val intent = Intent(this, MovieGenerator::class.java).apply {
                putStringArrayListExtra("movies", mov.distinct() as ArrayList<String>)
            }
            startActivity(intent)
        }
        else {
            Handler(Looper.getMainLooper()).postDelayed({
                checkProgress()
            }, 1000)
        }
    }

    private fun isDarkMode(context: Context): Boolean {
        val darkModeFlag =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    private fun selectLangCode(lang: String) {
        when (lang) {
            "English" -> originLang = "en"
            "Hindi" -> originLang = "hi"
            "Punjabi" -> originLang = "pa"
//            "French" -> originLang = "fr"
//            "Spanish" -> originLang = "es"
//            "German" -> originLang = "de"
//            "Italian" -> originLang = "it"
//            "Japanese" -> originLang = "ja"
//            "Korean" -> originLang = "ko"
//            "Chinese" -> originLang = "zh"
//            "Russian" -> originLang = "ru"
//            "Arabic" -> originLang = "ar"
//            "Portuguese" -> originLang = "pt"
//            "Dutch" -> originLang = "nl"
//            "Swedish" -> originLang = "sv"
//            "Danish" -> originLang = "da"
//            "Norwegian" -> originLang = "no"
//            "Finnish" -> originLang = "fi"
//            "Polish" -> originLang = "pl"
//            "Turkish" -> originLang = "tr"
//            "Czech" -> originLang = "cs"
//            "Greek" -> originLang = "el"
//            "Hebrew" -> originLang = "he"
//            "Hungarian" -> originLang = "hu"
//            "Romanian" -> originLang = "ro"
//            "Slovak" -> originLang = "sk"
//            "Thai" -> originLang = "th"
//            "Vietnamese" -> originLang = "vi"
//            "Indonesian" -> originLang = "id"
//            "Malay" -> originLang = "ms"
//            "Filipino" -> originLang = "tl"
//            "Urdu" -> originLang = "ur"
//            "Bengali" -> originLang = "bn"
//            "Gujarati" -> originLang = "gu"
//            "Hausa" -> originLang = "ha"
//            "Igbo" -> originLang = "ig"
//            "Kannada" -> originLang = "kn"
//            "Malayalam" -> originLang = "ml"
//            "Marathi" -> originLang = "mr"
//            "Tamil" -> originLang = "ta"
//            "Telugu" -> originLang = "te"
//            "Yoruba" -> originLang = "yo"
//            "Zulu" -> originLang = "zu"
        }
    }

    private fun getMovieTitles(year: String, language: String) {
        val quotesApi = RetrofitHelper.getInstance().create(MoviesApi::class.java)

        GlobalScope.launch {
            val result = quotesApi.getQuotes(primaryReleaseYear = year.toInt(), language = language)
            val pageSize = result.body()?.total_pages
            for (i in 1..pageSize!!) {
                val result2 = quotesApi.getQuotes(page = i,primaryReleaseYear = year.toInt(), withOriginalLanguage = language)
                val moviesList = result2.body()?.results
                for (movie in moviesList!!) {
                    titles = movie.title
                    mov.add(titles!!)
                }
                if (i == 100) {
                    allDone = true
                }else if (i == pageSize) {
                    allDone = true
                }
            }
        }
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

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Log.e("calender", "onDateSet: $p1 $p2 $p3")
    }

}