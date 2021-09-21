package com.miguelsantos.todewit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.miguelsantos.todewit.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        controller = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, controller)
    }

    override fun onSupportNavigateUp(): Boolean = controller.navigateUp()

}