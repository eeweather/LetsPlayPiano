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
/**
 *  Android memory game that plays an increasing series of sounds and lights, and prompts the user
 *  to input the same sequence. The game communicates with an ESP32 over Bluetooth which drives
 *  an array of NeoPixel LEDs that play short light shows based on the sound being played in order
 *  to provide a visual and audio cue to the player.
 *  By Emily Weatherford and Seth Rohrbach
 **/

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