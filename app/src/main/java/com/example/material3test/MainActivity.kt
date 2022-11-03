package com.example.material3test

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.material3test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavBar.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.GamesFragment,
            R.id.FriendsFragment,
            R.id.InventoryFragment,
            R.id.MarketFragment
        ))

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        /*navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.bottomNavBar.isVisible = destination.id != R.id.LoginFragment
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navController.getBackStackEntry("Games").run {
            println(destination)
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)

        /*return when (item.itemId) {
            R.id.SettingsFragment -> {
                val request = NavDeepLinkRequest.Builder
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
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }*/
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}