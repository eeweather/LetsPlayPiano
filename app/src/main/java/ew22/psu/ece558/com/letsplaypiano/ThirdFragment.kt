package ew22.psu.ece558.com.letsplaypiano

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

val twinkle = arrayOf("C4","C4","G4","G4")
var song = twinkle
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
        //game logic
        // computer play array[0]
        //user press array [0] check
        // app play 0 1
        // user press 0 check
        //user press 1 check

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


        binding.startbutton.setOnClickListener {
            round = 1
            userIndex = 0
            appGameHandler(round)
            userNotes = emptyArray<String>()

        }


        binding.gA4.setOnClickListener {
            pianoKeyPress(soundPool,a4, "a", requireContext())
            gameLogic("A4")

        }
        binding.gB4.setOnClickListener {
            pianoKeyPress(soundPool,b4, "b",requireContext())
            gameLogic("B4")

        }
        binding.gC4.setOnClickListener {
            pianoKeyPress(soundPool,c4, "c",requireContext())
            gameLogic("C4")

        }
        binding.gD4.setOnClickListener {
            pianoKeyPress(soundPool,d4, "d",requireContext())

           gameLogic("D4")

        }
        binding.gE4.setOnClickListener {
            pianoKeyPress(soundPool,e4, "e",requireContext())
            gameLogic("E4")
        }
        binding.gF4.setOnClickListener {
            pianoKeyPress(soundPool,f4, "f",requireContext())
            gameLogic("F4")
        }
        binding.gG4.setOnClickListener {
            pianoKeyPress(soundPool,g4, "g",requireContext())
            gameLogic("G4")

        }
        binding.gC5.setOnClickListener {
            pianoKeyPress(soundPool,c5, "h",requireContext())
            gameLogic("C5")

        }




    }

    fun gameHandler(round: Int){
        appGameHandler(round)
        userGameHandler(round)
    }
    fun userGameHandler(round: Int){
        for(i in 0..round){
            Log.i("user game handler :", i.toString())
            Log.i("user game handler:", song[i])


            if (song[i] != userNotes[i]){
                Toast.makeText(context, "END GAME", Toast.LENGTH_SHORT).show()
            }
        }
    }
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

    fun gameLogicOld(key: String){
        if(isPlayback == false){
            //userArray = append(userArray, "D4")
            Log.i("index:", round.toString())
            Log.i("user index:", userIndex.toString())
            if(userIndex<= round){
                Log.i("twinkle", song[userIndex])

                userNotes = append(userNotes, key)
                Log.i("in <=", userNotes[userIndex])

                if(userNotes[userIndex]== song[userIndex]){
                    Log.i("in ==", userNotes[userIndex])

                    userIndex++
                }
                else{
                    Toast.makeText(context, "YOU LOSE", Toast.LENGTH_SHORT).show()
                }
            }
            if(userIndex> round){
                Log.i("index:", round.toString())
                Log.i("user index:", userIndex.toString())
                Log.i("in <=", "In else")

                userNotes = emptyArray()
                userIndex= 0
                round++

                appGameHandler(round)


            }
        }

        isPlayback=false
    }


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

fun append(arr: Array<String>, element: String): Array<String> {
    val list: MutableList<String> = arr.toMutableList()
    list.add(element)
    return list.toTypedArray()
}

