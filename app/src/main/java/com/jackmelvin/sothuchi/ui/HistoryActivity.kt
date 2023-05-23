package com.jackmelvin.sothuchi.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jackmelvin.sothuchi.SoThuChiApplication
import com.jackmelvin.sothuchi.adapter.MoneyListAdapter
import com.jackmelvin.sothuchi.databinding.ActivityHistoryBinding
import com.jackmelvin.sothuchi.viewmodel.HistoryMoneyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val firstDayOfCurrentMonth = Calendar.getInstance().apply {
        set(get(Calendar.YEAR), get(Calendar.MONTH), 1, 0, 0, 0)
    }
    private val lastDayOfCurrentMonth = Calendar.getInstance().apply {
        set(get(Calendar.YEAR), get(Calendar.MONTH), getActualMaximum(Calendar.DAY_OF_MONTH), 0, 0, 0)
    }
    private val historyMoneyViewModel: HistoryMoneyViewModel by lazy {
        ViewModelProvider(this, HistoryMoneyViewModel.Factory(application as SoThuChiApplication))
            .get(HistoryMoneyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        // tvTimeRange
        initTextViewTimeRange()

        // tvPaidSummary, tvIncomeSummary
        initSummary()

        // tvBack
        initBackAction()

        initMoneyRecyclerView()

    }

    private fun initBackAction() {
        binding.tvBack.setOnClickListener {
            finish()
        }
    }

    private fun initMoneyRecyclerView() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity, LinearLayoutManager.VERTICAL, false)
        val moneyListAdapter = MoneyListAdapter {
            Toast.makeText(this@HistoryActivity, "Item clicked " + it.money.amount.toString(), Toast.LENGTH_SHORT).show()
        }
        binding.rvHistory.adapter = moneyListAdapter
        lifecycleScope.launch {
            historyMoneyViewModel.moneyHistory.collect {
                moneyListAdapter.submitList(it)
            }
        }
    }

    private fun initSummary() {
        // Set text view tvIncomeSummary and tvPaidSummary
        lifecycleScope.launch {
            val currentMonthBalance = historyMoneyViewModel.currentMonthBalance.first()
            withContext(Dispatchers.Main) {
                binding.tvIncomeSummary.text = currentMonthBalance.incomeSummary.toString()
                binding.tvPaidSummary.text = currentMonthBalance.paymentSummary.toString()
            }
//            val previousMonthBalance = historyMoneyViewModel.previousMonthBalance.first()
//            var incomeDiffStr = "∞%"
//            var paidDiffStr = "∞%"
//            var incomeDiffColor = ContextCompat.getColor(this@HistoryActivity, R.color.red)
//            var paidDiffColor = ContextCompat.getColor(this@HistoryActivity, R.color.green)
//            if (previousMonthBalance.incomeSummary != 0) {
//                val incomeDiff = currentMonthBalance.incomeSummary.toDouble() / previousMonthBalance.incomeSummary * 100 - 100
//                incomeDiffStr = String.format("%.2f%% (MoM)", incomeDiff)
//                if(incomeDiff >= 0.0) {
//                    incomeDiffStr = "+$incomeDiffStr"
//                    incomeDiffColor = ContextCompat.getColor(this@HistoryActivity, R.color.green)
//                }
//            }
//            if (previousMonthBalance.paidSummary != 0) {
//                val paidDiff = currentMonthBalance.paidSummary.toDouble() / previousMonthBalance.paidSummary * 100 - 100
//                paidDiffStr = String.format("%.2f%% (MoM)", paidDiff)
//                if(paidDiff > 0.0) {
//                    paidDiffStr = "+$paidDiffStr"
//                    paidDiffColor = ContextCompat.getColor(this@HistoryActivity, R.color.red)
//                }
//            }
//            withContext(Dispatchers.Main) {
//                binding.tvMomIncome.text = incomeDiffStr
//                binding.tvMomIncome.setTextColor(incomeDiffColor)
//                binding.tvMomPaid.text = paidDiffStr
//                binding.tvMomPaid.setTextColor(paidDiffColor)
//            }
        }
    }

    private fun initTextViewTimeRange() {
        // set text to tvTimeRange
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val timeRangeString = "${dateFormatter.format(firstDayOfCurrentMonth.time)} ~ ${dateFormatter.format(lastDayOfCurrentMonth.time)}"
        binding.tvTimeRange.text = timeRangeString
    }
}