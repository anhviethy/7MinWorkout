package com.via.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.via.a7minworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnCalculateUnit?.setOnClickListener {
            if (validateMetricUnits()) {
                val heightValue: Float =
                    binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue * heightValue)
                displayBMIResult(bmi)
            } else {
                Toast.makeText(this@BMIActivity, "Please enter valid values", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi <= 15f) {
            bmiLabel = "Very Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself!"
        } else if (bmi > 15f && bmi <= 16f) {
            bmiLabel = " Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself"
        } else if (bmi > 16f && bmi <= 18.5f) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself"
        } else if (bmi > 18.5f && bmi <= 25f) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape"
        } else {
            bmiLabel = "Overweight"
            bmiDescription =
                "Congratulations! You really need to take better care of yourself, workout now"
        }
        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text =
            BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDesciption?.text = bmiDescription
    }

    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (binding?.etMetricUnitWeight?.text.toString()
                .isEmpty() || binding?.etMetricUnitHeight?.text.toString().isEmpty()
        ) {
            isValid = false
        }
        return isValid
    }
}