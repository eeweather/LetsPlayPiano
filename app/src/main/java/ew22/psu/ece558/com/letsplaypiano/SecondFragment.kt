package ew22.psu.ece558.com.letsplaypiano

import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ew22.psu.ece558.com.letsplaypiano.databinding.FragmentSecondBinding

/**
 * Free play fragment
 * plays sounds when user presses keys
 * sets up soundpool for sounds**/

//init soundpool variable
private lateinit var soundPool: SoundPool

class SecondFragment : Fragment() {
    private val TAG: String = SecondFragment::class.java.simpleName
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        writeToBluetooth("x", requireContext())


            //soundpool set up, depends on SDK version, use soundpool builder if newer
        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.LOLLIPOP
        ) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(
                    AudioAttributes.USAGE_ASSISTANCE_SONIFICATION
                )
                .setContentType(
                    AudioAttributes.CONTENT_TYPE_SONIFICATION
                )
                .build()
            soundPool = SoundPool.Builder()
                .setMaxStreams(8)
                .setAudioAttributes(
                    audioAttributes
                )
                .build()
        } else {
            soundPool = SoundPool(
                8,
                AudioManager.STREAM_MUSIC,
                0
            )
        }

        //assign sounds
        val a4 = soundPool.load(context, R.raw.a4, 1 )
        val b4 = soundPool.load(context, R.raw.b4, 1 )
        val c4 = soundPool.load(context, R.raw.c4, 1 )
        val c5 = soundPool.load(context, R.raw.c5, 1 )
        val d4 = soundPool.load(context, R.raw.d4, 1 )
        val e4 = soundPool.load(context, R.raw.e4, 1 )
        val f4 = soundPool.load(context, R.raw.f4, 1 )
        val g4 = soundPool.load(context, R.raw.g4, 1 )

        //on click listeners that pressbutton and change color of button
        binding.A4.setOnClickListener {
            binding.A4.setBackgroundColor(Color.WHITE)
            pianoKeyPress(soundPool,a4, "a", requireContext())
            Handler().postDelayed({
                binding.A4.setBackgroundColor(0x4CAF50)
            }, 500)
        }
        binding.B4.setOnClickListener {
            binding.B4.setBackgroundColor(Color.WHITE)
            pianoKeyPress(soundPool,b4, "b",requireContext())
            Handler().postDelayed({
                binding.B4.setBackgroundColor(0x009688)
            }, 500)
        }
        binding.C4.setOnClickListener {
            binding.C4.setBackgroundColor(Color.WHITE)
            Handler().postDelayed({
                binding.C4.setBackgroundColor(0xFF5722)
            }, 500)
            pianoKeyPress(soundPool,c4, "c",requireContext())

        }
        binding.D4.setOnClickListener {
            binding.D4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,d4, "d",requireContext())
            Handler().postDelayed({
                binding.D4.setBackgroundColor(0xFF9800)
            }, 500)
        }
        binding.E4.setOnClickListener {
            binding.E4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,e4, "e",requireContext())
            Handler().postDelayed({
                binding.E4.setBackgroundColor(0xFFC107)
            }, 500)
        }
        binding.F4.setOnClickListener {
            binding.F4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,f4, "f",requireContext())
            Handler().postDelayed({
                binding.F4.setBackgroundColor(0xFFEB3B)
            }, 500)
        }
        binding.G4.setOnClickListener {
            binding.G4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,g4, "g",requireContext())
            Handler().postDelayed({
                binding.G4.setBackgroundColor(0xCDDC39)
            }, 500)
        }
        binding.C5.setOnClickListener {
            binding.C5.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,c5, "h",requireContext())
            Handler().postDelayed({
                binding.C5.setBackgroundColor(0x00BCD4)
            }, 500)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

// global function to play sound and write to bluetooth to be used in multiple fragments
fun pianoKeyPress(soundPool: SoundPool, sound: Int, message: String, context: Context){
    soundPool.play(sound, 1f, 1f, 0, 0,1f)
    //send bluetooth (toast for now)
    //Toast.makeText(context, "Writing '${message}' to bluetooth", Toast.LENGTH_SHORT).show()
    writeToBluetooth(message, context)

}