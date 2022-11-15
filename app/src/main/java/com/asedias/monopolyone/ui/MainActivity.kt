package com.asedias.monopolyone.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.asedias.monopolyone.R
import com.asedias.monopolyone.api.MonopolyWebSocket
import com.asedias.monopolyone.databinding.ActivityMainBinding
import com.asedias.monopolyone.repository.MainRepository
import com.asedias.monopolyone.ui.viewmodel.MainActivityViewModel
import com.asedias.monopolyone.util.AuthStoreManager
import com.asedias.monopolyone.util.WSState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    val authStoreManager by lazy {
        AuthStoreManager(this, this)
    }

    private val mainRepository by lazy {
        MainRepository()
    }

    private val viewModel: MainActivityViewModel by viewModels() {
        MainActivityViewModel.ProviderFactory(mainRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        authStoreManager.toString()

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

        MonopolyWebSocket().start()
        lifecycleScope.launchWhenStarted {
            MonopolyWebSocket.state.collectLatest {
                when(it) {
                    is WSState.Open -> Log.d("MonopolyWebSocket", "Open")
                    is WSState.Connected -> Log.d("MonopolyWebSocket", "Connected")
                    is WSState.Failure -> {
                        Log.d("MonopolyWebSocket", it.t.localizedMessage)
                    }
                    is WSState.Closed -> Log.d("MonopolyWebSocket", "Closed cause ${it.reason}")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}