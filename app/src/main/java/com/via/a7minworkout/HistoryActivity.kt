package com.via.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.via.a7minworkout.databinding.ActivityFinnishBinding
import com.via.a7minworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistoryActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (application as WorkOutApp).db.historyDao()

        getAllCompletedDates(dao)
    }

    private fun getAllCompletedDates(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.getAllHistory().collect {
                if (it.isNotEmpty()) {
                    binding?.rcvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    val dates = ArrayList(it)

                    val historyAdapter = HistoryAdapter(dates)
                    binding?.rcvHistory?.adapter = historyAdapter
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}