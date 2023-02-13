package com.portfolio.someapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.datepicker.MaterialDatePicker
import com.portfolio.someapplication.databinding.ActivityMainBinding
import com.portfolio.someapplication.fragments.ProfileFragment
import com.portfolio.someapplication.fragments.QuestionFragment
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                R.string.app_name,
                R.string.app_name
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.profile -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Profile",
                            Toast.LENGTH_SHORT
                        ).show()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, ProfileFragment())
                            .addToBackStack(null)
                            .commit()
                    }

                    R.id.settings -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Settings",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    R.id.rating -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Rate us",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                true
            }
        }
        setUpDatePicker()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpDatePicker() {
        binding.fab.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                Log.d("DATEPICKER", datePicker.headerText)
                val dateFormatter = SimpleDateFormat("dd-mm-yyyy")
                val date = dateFormatter.format(Date(it))
                val intent = Intent(this, QuestionFragment::class.java)
                intent.putExtra("DATE", date)
                startActivity(intent)
            }
            datePicker.addOnNegativeButtonClickListener {
                Log.d("DATEPICKER", datePicker.headerText)

            }
            datePicker.addOnCancelListener {
                Log.d("DATEPICKER", "Date Picker Cancelled")
            }
        }
    }
}