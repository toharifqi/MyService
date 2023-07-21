package com.dicoding.naufal.myservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.dicoding.naufal.myservice.databinding.ActivityMainBinding
import com.dicoding.naufal.myservice.service.MyBoundService
import com.dicoding.naufal.myservice.service.MyJobIntentService
import com.dicoding.naufal.myservice.service.MyService

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private var mServiceBound = false
    private lateinit var mBoundService: MyBoundService

    private val mServiceConnection = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            mServiceBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as MyBoundService.MyBinder
            mBoundService = myBinder.getService
            mServiceBound = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartService.setOnClickListener(this)
        binding.btnStartJobIntentService.setOnClickListener(this)
        binding.btnStartBoundService.setOnClickListener(this)
        binding.btnStopBoundService.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btn_start_service -> {
                    val mStartServiceIntent = Intent(this, MyService::class.java)
                    startService(mStartServiceIntent)
                }
                R.id.btn_start_job_intent_service -> {
                    val mStartIntentService = Intent(this, MyJobIntentService::class.java)
                    mStartIntentService.putExtra(MyJobIntentService.EXTRA_DURATION, 5000L)
                    MyJobIntentService.enqueueWork(this, mStartIntentService)
                }
                R.id.btn_start_bound_service -> {
                    val mBoundServiceIntent = Intent(this, MyBoundService::class.java)
                    bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
                }
                R.id.btn_stop_bound_service -> {
                    unbindService(mServiceConnection)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mServiceBound){
            unbindService(mServiceConnection)
        }
    }
}