package ew22.psu.ece558.com.letsplaypiano

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import ew22.psu.ece558.com.letsplaypiano.databinding.FragmentSecondBinding

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

        val a4 = soundPool.load(context, R.raw.a4, 1 )
        val b4 = soundPool.load(context, R.raw.b4, 1 )
        val c4 = soundPool.load(context, R.raw.c4, 1 )
        val c5 = soundPool.load(context, R.raw.c5, 1 )
        val d4 = soundPool.load(context, R.raw.d4, 1 )
        val e4 = soundPool.load(context, R.raw.e4, 1 )
        val f4 = soundPool.load(context, R.raw.f4, 1 )
        val g4 = soundPool.load(context, R.raw.g4, 1 )

        binding.A4.setOnClickListener {
            pianoKeyPress(soundPool,a4, "a", requireContext())
        }
        binding.B4.setOnClickListener {
            pianoKeyPress(soundPool,b4, "b",requireContext())
        }
        binding.C4.setOnClickListener {
            pianoKeyPress(soundPool,c4, "c",requireContext())
        }
        binding.D4.setOnClickListener {
            pianoKeyPress(soundPool,d4, "d",requireContext())
        }
        binding.E4.setOnClickListener {
            pianoKeyPress(soundPool,e4, "e",requireContext())
        }
        binding.F4.setOnClickListener {
            pianoKeyPress(soundPool,f4, "f",requireContext())
        }
        binding.G4.setOnClickListener {
            pianoKeyPress(soundPool,g4, "g",requireContext())
        }
        binding.C5.setOnClickListener {
            pianoKeyPress(soundPool,c5, "h",requireContext())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

fun pianoKeyPress(soundPool: SoundPool, sound: Int, message: String, context: Context){
    soundPool.play(sound, 1f, 1f, 0, 0,1f)
    //send bluetooth (toast for now)
    //Toast.makeText(context, "Writing '${message}' to bluetooth", Toast.LENGTH_SHORT).show()
    writeToBluetooth(message, context)

}