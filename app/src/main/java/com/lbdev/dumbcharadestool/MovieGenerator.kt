package com.lbdev.dumbcharadestool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.lbdev.dumbcharadestool.databinding.ActivityMovieGeneratorBinding

class MovieGenerator : AppCompatActivity() {
    lateinit var _binding: ActivityMovieGeneratorBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMovieGeneratorBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val drawerLayout = _binding.drawerLayout
        val navView = _binding.navView

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_stopwatch -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    MovieGenerator().finish()
                }
                R.id.nav_movieGenerator -> drawerLayout.closeDrawers()
                R.id.nav_exit -> finish()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}