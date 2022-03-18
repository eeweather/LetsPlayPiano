package ew22.psu.ece558.com.letsplaypiano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ew22.psu.ece558.com.letsplaypiano.databinding.FragmentFifthBinding

/**
 * You Win fragment
 * buttons to take you to game mode or main menu**/

class fifthFragment : Fragment() {
    private val TAG: String = fifthFragment::class.java.simpleName

    private var _binding: FragmentFifthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFifthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        writeToBluetooth("w", requireContext())

        binding.mainMenuBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fifthFragment_to_firstFragment)
            writeToBluetooth("z",requireContext())
        }

        binding.playAgainBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fifthFragment_to_thirdFragment)
        }
    }
}