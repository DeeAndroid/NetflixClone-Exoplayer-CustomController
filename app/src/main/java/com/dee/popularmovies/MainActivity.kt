package com.dee.popularmovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.dee.popularmovies.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

private lateinit var binding: ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment


        /** handling the bottomnavigation visibility */
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { controller, _, _ ->
            when (controller.currentDestination?.id) {
                R.id.movieListFragment -> {
                    binding.bottomNavHome.visibility = View.VISIBLE
                }

                else -> binding.bottomNavHome.visibility = View.GONE
            }
        }

        NavigationUI.setupWithNavController(
            binding.bottomNavHome,
            navController
        )

    }
}