package com.example.material3test.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.material3test.R
import com.example.material3test.databinding.ActivityMainBinding
import com.example.material3test.repository.MainRepository
import com.example.material3test.ui.viewmodel.MainActivityViewModel
import com.example.material3test.util.Auth
import com.example.material3test.util.AuthStoreManager
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var authStoreManager: AuthStoreManager

    private val mainRepository by lazy {
        MainRepository()
    }

    private val viewModel: MainActivityViewModel by viewModels() {
        MainActivityViewModel.ProviderFactory(mainRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        authStoreManager = AuthStoreManager(this, this)

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

        Auth.observableUserLogged.observe(this) {
            if(it) viewModel.getAccountInfo()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.account.collectLatest { account ->
                account.run {
                    if(this != null) {
                        println("Retrieve account ${nick}@${user_id}")
                    }
                }
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}