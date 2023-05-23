package com.jackmelvin.sothuchi.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.SoThuChiApplication
import com.jackmelvin.sothuchi.databinding.ActivityMainBinding
import com.jackmelvin.sothuchi.viewmodel.MainBalanceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainBalanceViewModel: MainBalanceViewModel by lazy {
        ViewModelProvider(this, MainBalanceViewModel.Factory(application as SoThuChiApplication))
            .get(MainBalanceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initDBRelatedViews()
    }

    private fun initViews() {
        setDate()
        setButtonAction()
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        val dateString = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)+1} ($dayOfWeek)"
        binding.tvDate.text = dateString
    }

    private fun setButtonAction() {
        binding.clInput.setOnClickListener {
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            startActivity(intent)
        }
        binding.btnHistory.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initDBRelatedViews() {
        lifecycleScope.launch {
            mainBalanceViewModel.currentMonthBalance.collect {
                withContext(Dispatchers.Main) {
                    binding.tvPaid.text = it.paymentSummary.toString()
                    binding.tvIncome.text = it.incomeSummary.toString()
                    binding.tvBalance.text = (it.incomeSummary - it.paymentSummary).toString()
                }
            }
        }
    }

    private fun getDayOfWeek(i: Int): String {
        return resources.getStringArray(R.array.dayOfWeek)[i-1]
    }
}