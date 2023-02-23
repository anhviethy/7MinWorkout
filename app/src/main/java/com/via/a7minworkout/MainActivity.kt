package com.via.a7minworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.via.a7minworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding.flStart.setOnClickListener {
            Toast.makeText(this, "Start clicked", Toast.LENGTH_LONG).show()
        }
    }
}