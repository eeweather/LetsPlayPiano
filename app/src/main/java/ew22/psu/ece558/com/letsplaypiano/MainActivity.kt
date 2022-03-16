package ew22.psu.ece558.com.letsplaypiano

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import ew22.psu.ece558.com.letsplaypiano.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBarWithNavController(findNavController(R.id.main_fragment))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_fragment)
        return navController.navigateUp() || return onSupportNavigateUp()
    }
}