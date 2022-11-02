package com.example.material3test

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.material3test.databinding.ActivityMainBinding
import com.example.material3test.fragment.BlankFragment
import com.example.material3test.navigation.KeepStateNavigator

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var gamesBundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavBar.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.Games,
            R.id.Friends,
            R.id.Inventory,
            R.id.Market
        ))

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun setupGraph() {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return onNavDestinationSelected(item, navController);
        navController.getBackStackEntry("Games").run {
            println(destination)
        }
        
        return when (item.itemId) {
            R.id.SettingsFragment -> {
                /*val request = NavDeepLinkRequest.Builder
                    .fromUri("android-app://com.asedias.monopolyone/settings".toUri())
                    .build()
                navController.navigate(request, navOptions {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(popUpToId, popUpToBuilder = {
                        inclusive = true
                    })
                })
                navController.navigate(R.id.action_GamesFragment_to_SettingsFragment, Bundle.EMPTY, navOptions {
                    popUpTo(popUpToId, popUpToBuilder = {
                        inclusive = true
                    })
                })*/
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}