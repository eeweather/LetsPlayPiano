package ew22.psu.ece558.com.letsplaypiano

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import ew22.psu.ece558.com.letsplaypiano.databinding.FragmentFirstBinding
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

private const val TAG = "MY_APP_DEBUG_TAG"

// Defines several constants used when transmitting messages between the
// service and the UI.
const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2
const val REQUEST_ENABLE_BT: Int = 1

var mySocket:BluetoothSocket? = null
val myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
var myDevice: BluetoothDevice? = null

var writeString = ""

class FirstFragment : Fragment() {
    private val TAG: String = FirstFragment::class.java.simpleName

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)






    fun getBlueToothConnection(){
        //get bluetooth adapter
        @SuppressWarnings("deprecation")
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(context, "No Bluetooth", Toast.LENGTH_SHORT).show()
        }


        //enable bluetooth, could add startactivityforresult here, but its depreciated?
        if (bluetoothAdapter?.isEnabled == false) {
            Toast.makeText(context, "Bluetooth is disabled", Toast.LENGTH_SHORT).show()
        }

        //check android12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT))
        }
        else{
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }




        //query paired devices
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(context, "Bluetooth Permission Error", Toast.LENGTH_SHORT).show()

        }
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            if(mySocket == null && device.name == "ESP32test"){
                try {
                    myDevice = device
                    Log.i(device.name, mySocket?.isConnected.toString())
                    mySocket = myDevice?.createInsecureRfcommSocketToServiceRecord(myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    mySocket!!.connect()
                    Log.i(device.name, mySocket?.isConnected.toString())
                } catch(e: IOException){
                    mySocket = null
                    e.printStackTrace()
                }

            }
        }
    }


        binding.freeplaybtn.setOnClickListener {


            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }
        binding.gamebtn.setOnClickListener {

            findNavController().navigate(R.id.action_firstFragment_to_thirdFragment)
        }
        binding.bluetoothbtn.setOnClickListener {
            getBlueToothConnection()
            writeToBluetooth("z",requireContext())
        }
    }

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Toast.makeText(context, "Result Code: ${result.resultCode.toString()}", Toast.LENGTH_SHORT).show()
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
                Toast.makeText(context, "Result: ${it.key} = ${it.value}", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

fun writeToBluetooth(writeString:String, context: Context){
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        Toast.makeText(context, "Bluetooth Permission Error", Toast.LENGTH_SHORT).show()
        return
    }
    mySocket?.outputStream?.write(writeString.toByteArray())
    //Toast.makeText(context, "${myDevice?.address.toString()} - ${myDevice?.name.toString()} - ${writeString}", Toast.LENGTH_SHORT).show()
}
