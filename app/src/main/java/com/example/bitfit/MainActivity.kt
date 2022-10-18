package com.example.bitfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // define fragments
        val logFragment : Fragment = LogFragment()
        val dashboardFragment : Fragment = DashboardFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)


        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_log -> fragment = logFragment
                R.id.nav_dashboard -> fragment = dashboardFragment
            }
            replaceFragment(fragment)
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.nav_log

        findViewById<Button>(R.id.deleteAll).setOnClickListener {
            lifecycleScope.launch(IO) {
                (application as BitfitApplication).db.sleepDao().deleteAll()
            }
        }

        findViewById<Button>(R.id.record).setOnClickListener {
            val intent = Intent(this, SleepActivity::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.basic_frame_layout, fragment)
        fragmentTransaction.commit()
    }

    fun delete(sleepItem : SleepItem) {
        lifecycleScope.launch(IO) {
//            (application as BitfitApplication).db.sleepDao().getAll().collect { databaseList ->
//                databaseList[position].d
//            }
            (application as BitfitApplication).db.sleepDao().delete(
                SleepEntity(
                    hours = sleepItem.hours,
                    date = sleepItem.date,
                    notes = sleepItem.notes
                )
            )
        }
    }

}