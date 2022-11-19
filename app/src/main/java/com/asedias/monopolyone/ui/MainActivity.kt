package com.asedias.monopolyone.ui

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
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.asedias.monopolyone.R
import com.asedias.monopolyone.api.MonopolyWebSocket
import com.asedias.monopolyone.databinding.ActivityMainBinding
import com.asedias.monopolyone.ui.fragment.LoginBottomSheet
import com.asedias.monopolyone.ui.viewmodel.MainActivityViewModel
import com.asedias.monopolyone.util.SessionManager
import com.asedias.monopolyone.util.getCacheImageLoader
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavBar.setupWithNavController(navController)

        binding.appBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(this);
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.GamesFragment,
                R.id.FriendsFragment,
                R.id.InventoryFragment,
                R.id.MarketFragment
            )
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        observeUserData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        viewModel.avatarDrawable?.let {
            menu.getItem(0).icon = it
        }
        menu.getItem(0).setOnMenuItemClickListener {
            if (!SessionManager.isUserLogged()) {
                LoginBottomSheet().show(supportFragmentManager, LoginBottomSheet.TAG)
                return@setOnMenuItemClickListener true
            }
            Snackbar.make(
                binding.bottomNavBar,
                SessionManager.getAccessToken(),
                Snackbar.LENGTH_INDEFINITE
            ).apply { anchorView = binding.bottomNavBar }.show()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun observeUserData() = lifecycleScope.launchWhenStarted {
        viewModel.userData.collect { message ->
            if (message.status > 0) {
                val req = ImageRequest.Builder(this@MainActivity)
                    .data(MonopolyWebSocket.authMessage!!.user_data.avatar)
                    .decoderFactory(SvgDecoder.Factory())
                    .transformations(CircleCropTransformation())
                    .build()
                viewModel.avatarDrawable = getCacheImageLoader().execute(req).drawable
                invalidateOptionsMenu()
            }
        }
    }
}