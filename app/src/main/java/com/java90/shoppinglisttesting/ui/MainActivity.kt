package com.java90.shoppinglisttesting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.java90.shoppinglisttesting.R
import com.java90.shoppinglisttesting.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onResume() {
        handleBackPress()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun handleBackPress() = this.onBackPressedDispatcher.addCallback(object :
        OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController.currentDestination?.let { current ->
                when(current.id) {
                    R.id.shoppingFragment -> finish()
                    else -> navController.navigateUp()
                }
            }
        }
    })
}