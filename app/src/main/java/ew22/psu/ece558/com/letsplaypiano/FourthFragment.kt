package ew22.psu.ece558.com.letsplaypiano

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import ew22.psu.ece558.com.letsplaypiano.databinding.FragmentFourthBinding

/**
 * You lose fragment
 * buttons to take you to game mode or main menu**/
class FourthFragment : Fragment() {
    private val TAG: String = FourthFragment::class.java.simpleName

    private var _binding: FragmentFourthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFourthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        writeToBluetooth("y", requireContext())

        binding.mainMenuBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fourthFragment_to_firstFragment)
            writeToBluetooth("z",requireContext())
        }

        binding.playAgainBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fourthFragment_to_thirdFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}