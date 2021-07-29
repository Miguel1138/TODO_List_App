package com.miguelsantos.todewit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CREATE_MEW_TASK = 1010
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        controller = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        controller = this.findNavController(R.id.myNavHostFragment)
        return controller.navigateUp()
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_MEW_TASK && resultCode == Activity.RESULT_OK) updateList()
        super.onActivityResult(requestCode, resultCode, data)
    }*/

}