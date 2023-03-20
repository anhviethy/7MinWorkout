package com.via.a7minworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.via.a7minworkout.databinding.ActivityExerciseBinding
import com.via.a7minworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 1

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    var exerciseTimerDuration: Long = 1

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var curExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null


    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()
        tts = TextToSpeech(this, this)

        binding?.toolBarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        setUpRestView()
        setUpExerciseStatusRecyclerView()
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val bindingDialog = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(bindingDialog.root)
        customDialog.setCanceledOnTouchOutside(false)
        bindingDialog?.btnYes?.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }

        bindingDialog?.btnNo?.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

    private fun setUpExerciseStatusRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setUpRestView() {
        binding?.flRestView?.visibility = View.VISIBLE
        binding?.flExercise?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE
        binding?.ivExercise?.visibility = View.INVISIBLE
        binding?.tvUpComing?.visibility = View.VISIBLE
        binding?.tvUpComingExerciseName?.visibility = View.VISIBLE

        try {
            val soundURI = Uri.parse("android.resource://com.via.a7minworkout/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.tvUpComingExerciseName?.text = exerciseList!![curExercisePosition + 1].getName()
        setRestProgressBar()
    }

    private fun setUpExerciseView() {
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.flExercise?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE
        binding?.ivExercise?.visibility = View.VISIBLE

        binding?.tvUpComing?.visibility = View.INVISIBLE
        binding?.tvUpComingExerciseName?.visibility = View.INVISIBLE
        binding?.ivExercise?.setImageResource(exerciseList!![curExercisePosition].getImage())
        binding?.tvExercise?.text = exerciseList!![curExercisePosition].getName()

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![curExercisePosition].getName())

        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(restTimerDuration * 1000, 1000) {
            override fun onTick(remainingTime: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                curExercisePosition++
                exerciseList!![curExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setUpExerciseView()
            }

        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.exerciseProgressBar?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000, 1000) {
            override fun onTick(remainingTime: Long) {
                exerciseProgress++
                binding?.exerciseProgressBar?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (curExercisePosition < exerciseList!!.size - 1) {
                    exerciseList!![curExercisePosition].setIsSelected(false)
                    exerciseList!![curExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setUpRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }

            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val res = tts?.setLanguage(Locale.US)

            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported!")
            }
        } else {
            Log.e("tts", "Initialization Failed")
        }
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}