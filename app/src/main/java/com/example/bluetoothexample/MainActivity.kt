package com.example.bluetoothexample

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var mStatusBluetoothTv: TextView
    lateinit var mPairedTv: TextView
    lateinit var mBlueIv: ImageView
    lateinit var mOnButton: Button
    lateinit var mOffButton: Button
    lateinit var mDiscoverButton: Button
    lateinit var mPairedButton: Button

    lateinit var mBlueAdapter : BluetoothAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mStatusBluetoothTv = findViewById(R.id.statusBluetoothTv)
        mPairedTv= findViewById(R.id.pairedTv)
        mBlueIv= findViewById(R.id.bluetoothIv)
        mOnButton = findViewById(R.id.onButton)
        mOffButton = findViewById(R.id.offButton)
        mDiscoverButton = findViewById(R.id.discoverableButton)
        mPairedButton = findViewById(R.id.pairedButton)

        //Adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter()

        //Check if bluetooth is available or not
        if(mBlueAdapter == null)
        {
            mStatusBluetoothTv.text = "Bluetooth is not available"
        }
        else{
            mStatusBluetoothTv.text = "Bluetooth is available"
        }

        //set image according to bluetooth status(on/off)
        if(mBlueAdapter.isEnabled()){
            mBlueIv.setImageResource(R.drawable.ic_action_on)
        }
        else{
            mBlueIv.setImageResource(R.drawable.ic_action_off)
        }

        //On button click
        mOnButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(!mBlueAdapter.isEnabled()){
                    showToast("Turning On Bluetooth...")
                        //intent to on bluetooth
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(intent, REQUEST_ENABLE_BT)
                }
                else{
                    showToast("Bluetooth is already on")
                }
            }
        })
        // discover bluetooth button click
        mDiscoverButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(!mBlueAdapter.isDiscovering()){
                    showToast("Making Your Device Discoverable")
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                    startActivityForResult(intent, REQUEST_DISCOVER_BT)
                }
            }
        })
        //Off button click
        mOffButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(mBlueAdapter.isEnabled()){
                    mBlueAdapter.disable()
                    showToast("Turning Bluetooth off")
                    mBlueIv.setImageResource(R.drawable.ic_action_off)
                    mPairedTv.text=""
                }
                else{
                    showToast("Bluetooth is already off")
                }
            }
        })
        //Get paired devices button click
        mPairedButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(mBlueAdapter.isEnabled){
                    mPairedTv.text = "Paired Devices"
                    for(BluetoothDevice in mBlueAdapter.bondedDevices){
                        mPairedTv.append("\nDevice: " + BluetoothDevice.name + "," + BluetoothDevice)

                    }
                }
                else{
                    //Bluetooth is off so can't get paired devices
                    showToast("Turn on bluetooth to get paired devices")
                }
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_ENABLE_BT -> {
                if(resultCode == Activity.RESULT_OK){
                    //bluetooth is on
                    mBlueIv.setImageResource((R.drawable.ic_action_on))
                    showToast("Bluetooth is on")
                }
                else{
                    //user denied to turn bluetooth on
                    showToast("Could not on bluetooth")
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //toast message function
    fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 0;
        private const val REQUEST_DISCOVER_BT = 1;
    }
}