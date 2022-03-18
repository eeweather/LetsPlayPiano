package ew22.psu.ece558.com.letsplaypiano

import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import ew22.psu.ece558.com.letsplaypiano.databinding.FragmentThirdBinding
import java.util.ArrayList


/**
 * Game mode fragment
 * same as freeplay but with a start button and game logic
 * has helper functions for game logic, plays game song, generating random song**/

val twinkle = arrayOf("C4","C4","G4","G4")
var song = randomSong(10)
var round = 0
var userIndex = 0
var isPlayback = false


class ThirdFragment : Fragment() {
    private val TAG: String = ThirdFragment::class.java.simpleName
    private lateinit var soundPool: SoundPool
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!
    var userNotes = emptyArray<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    //sound pool set up same as second fragment
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

        //start button start game by moving round to 1
        binding.startbutton.setOnClickListener {
            round = 1
            userIndex = 0
            appGameHandler(round)
            userNotes = emptyArray<String>()

        }

        //button listeners same as in fragment two but with gamelogic function
        binding.gA4.setOnClickListener {
            binding.gA4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,a4, "a", requireContext())
            Handler().postDelayed({
                binding.gA4.setBackgroundColor(0x4CAF50)
            }, 500)
            gameLogic("A4")

        }
        binding.gB4.setOnClickListener {
            binding.gB4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,b4, "b",requireContext())
            Handler().postDelayed({
                binding.gB4.setBackgroundColor(0x009688)
            }, 500)
            gameLogic("B4")

        }
        binding.gC4.setOnClickListener {
            binding.gC4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,c4, "c",requireContext())
            Handler().postDelayed({
                binding.gC4.setBackgroundColor(0xFF5722)
            }, 500)
            gameLogic("C4")

        }
        binding.gD4.setOnClickListener {
            binding.gD4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,d4, "d",requireContext())
            Handler().postDelayed({
                binding.gD4.setBackgroundColor(0xFF9800)
            }, 500)

           gameLogic("D4")

        }
        binding.gE4.setOnClickListener {
            binding.gE4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,e4, "e",requireContext())
            Handler().postDelayed({
                binding.gE4.setBackgroundColor(0xFFC107)
            }, 500)
            gameLogic("E4")
        }
        binding.gF4.setOnClickListener {
            binding.gF4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,f4, "f",requireContext())
            Handler().postDelayed({
                binding.gF4.setBackgroundColor(0xFFEB3B)
            }, 500)
            gameLogic("F4")
        }
        binding.gG4.setOnClickListener {
            binding.gG4.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,g4, "g",requireContext())
            Handler().postDelayed({
                binding.gG4.setBackgroundColor(0xCDDC39)
            }, 500)
            gameLogic("G4")

        }
        binding.gC5.setOnClickListener {
            binding.gC5.setBackgroundColor(Color.WHITE)

            pianoKeyPress(soundPool,c5, "h",requireContext())
            Handler().postDelayed({
                binding.gC5.setBackgroundColor(0x00BCD4)
            }, 500)
            gameLogic("C5")

        }




    }

    //plays song from the app, intakes how many notes to press and then presses those keys
    fun appGameHandler(round: Int){
        for (i in 0..(round - 1)){
            var note = song[i]
            Handler().postDelayed({
                isPlayback = true
                when(note){
                    "C4" -> {
                        binding.gC4.performClick()}
                    "D4" -> {
                        binding.gD4.performClick()}
                    "E4" -> {
                        binding.gE4.performClick()}
                    "F4" -> {
                        binding.gF4.performClick()}
                    "G4" -> {
                        binding.gG4.performClick()}
                    "A4" -> {
                        binding.gA4.performClick()}
                    "B4" -> {
                        binding.gB4.performClick()}
                    "C5" -> {
                        binding.gC5.performClick()}
                }
            }, (1000*(i+1)).toLong())
        }
    }


    // manages game logic, checks if game over, win/lose, plays song for next round and starts next round
    fun gameLogic(currentNote: String){
        if(isPlayback == true) {
            isPlayback = false
            return
        }

        userNotes = append(userNotes, currentNote)

        val compareNote = song[userNotes.size - 1]

        if(currentNote != compareNote){
            Toast.makeText(context, "YOU LOSE", Toast.LENGTH_SHORT).show()
            userNotes = emptyArray()
            round = 0
            findNavController().navigate(R.id.action_thirdFragment_to_fourthFragment)
            return
        }

        if(userNotes.size != round) {
            Log.i("waiting for next note ${round}", song.toString())
            return
        }
        Log.i("round passed! ${song[round - 1]}", userNotes.toString())
        userNotes = emptyArray()

        // check if song complete
        if(song.size == round){
            Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show()
            //appGameHandler(round)
            round = 0
            findNavController().navigate(R.id.action_thirdFragment_to_fifthFragment)
            return
        }

        // move to next round
        round++
        appGameHandler(round)
    }

}

//generates a random song of the provided keys at an input length
fun randomSong(length: Int): Array<String>{
    var newSong = Array(length){_ -> ""}
    var notes = arrayOf("C4","D4","E4","F4","G4","A4","B4","C5")

    for (i in 0..(length - 1)){
        val rand = (0..(notes.size - 1)).random()
        Log.i(rand.toString(), length.toString())
        val note = notes[rand]
        newSong[i] = note
    }

    Log.i("randomSong ${length.toString()}", newSong.toString())
    return newSong
}

//helper append funtion
fun append(arr: Array<String>, element: String): Array<String> {
    val list: MutableList<String> = arr.toMutableList()
    list.add(element)
    return list.toTypedArray()
}

