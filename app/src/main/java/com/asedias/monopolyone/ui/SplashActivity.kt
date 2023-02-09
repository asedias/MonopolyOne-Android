package com.asedias.monopolyone.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.asedias.monopolyone.R
import com.asedias.monopolyone.data.remote.WebSocketClient
import com.asedias.monopolyone.data.repository.AuthRepositoryImpl
import com.asedias.monopolyone.ui.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var authRepositoryImpl: AuthRepositoryImpl

    @Inject
    lateinit var webSocketClient: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launch { viewModel.collectRequires() }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        val log = findViewById<TextView>(R.id.logText)
        viewModel.state.observe(this) { state ->
            log.text = getString(state.text)
            if(state.startMainActivity) {
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}