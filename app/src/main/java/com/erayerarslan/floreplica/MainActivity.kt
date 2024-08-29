package com.erayerarslan.floreplica

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import android.window.OnBackInvokedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.erayerarslan.floreplica.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // BottomNavigationView ile NavController'ı bağla
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.categoryFragment -> {
                    navController.navigate(R.id.categoryFragment)
                    true
                }
                R.id.favoriteFragment -> {
                    navController.navigate(R.id.favoriteFragment)
                    true
                }
                R.id.profileHomeFragment -> {
                    navController.navigate(R.id.profileHomeFragment)
                    true
                }
                else -> false
            }
        }


    }






    private fun createBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigation,navHostFragment.navController)


    }


    fun showBottomNavigationView() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    fun hideBottomNavigationView() {
        binding.bottomNavigation.visibility = View.GONE
    }




    override fun onSupportNavigateUp(): Boolean {

        return  navController.navigateUp() ||   super.onSupportNavigateUp()
    }
}

