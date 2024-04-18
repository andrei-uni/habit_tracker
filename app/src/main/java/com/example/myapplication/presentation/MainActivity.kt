package com.example.myapplication.presentation

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.syncers.AddedHabitsSyncer
import com.example.myapplication.data.syncers.UpdatedHabitsSyncer
import com.example.myapplication.databinding.MainActivityBinding
import com.example.myapplication.utils.Constants
import com.example.myapplication.utils.Dependencies

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        setAvatar()

        Dependencies.init(applicationContext)

        AddedHabitsSyncer.init()
        UpdatedHabitsSyncer.init()
    }

    private fun setupNavigation() {
        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.aboutFragment,
            ),
            binding.drawerLayout,
        )

        navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)
    }

    private fun setAvatar() {
        val headerView = binding.navigationView.getHeaderView(0)
        val imageView = headerView.findViewById<ImageView>(R.id.avatar_imageview)

        Glide.with(this)
            .load(Constants.AVATAR_URL)
            .placeholder(R.drawable.avatar_placeholder)
            .error(R.drawable.avatar_error)
            .centerCrop()
            .circleCrop()
            .into(imageView)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}