package com.jackmelvin.sothuchi.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.adapter.MoneyAdapter
import com.jackmelvin.sothuchi.database.AppDatabase
import com.jackmelvin.sothuchi.databinding.ActivityHistoryBinding
import com.jackmelvin.sothuchi.model.Money
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var firstDayOfMonth: Calendar
    private lateinit var lastDayOfMonth: Calendar
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        db = AppDatabase.getInstance(this@HistoryActivity)
        // tvTimeRange
        initTimeRange()

        // tvPaidSummary, tvIncomeSummary
        initSummary()

        // tvBack
        initBackAction()

        initMoneyRecyclerView()

    }

    private fun initBackAction() {
        binding.tvBack.setOnClickListener {
            startActivity(Intent(this@HistoryActivity, MainActivity::class.java))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initMoneyRecyclerView() {
        val moneyHistoryList = mutableListOf<Money>()
        val moneyAdapter = MoneyAdapter(moneyHistoryList)
        lifecycleScope.launch(Dispatchers.IO) {
            val dbMoneyList = db.moneyDao().findAllInTimeRange(MainActivity.USER_ID, firstDayOfMonth, lastDayOfMonth)
            if(dbMoneyList.isNotEmpty()) {
                moneyHistoryList.addAll(dbMoneyList)
                moneyAdapter.notifyDataSetChanged()
            }
        }
        binding.rvHistory.adapter = moneyAdapter
        binding.rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity, LinearLayoutManager.VERTICAL, false)
    }

    private fun initSummary() {
        // Money related fields (got from MainActivity to reduce DB queries)
        val bundle = intent.extras
        binding.tvIncomeSummary.text = bundle?.getInt(MainActivity.INCOME_SUMMARY)?.toString()
        binding.tvPaidSummary.text = bundle?.getInt(MainActivity.PAID_SUMMARY)?.toString()

        /*
        if last month balance DOES NOT exists,
            hide tvMomIncome and tvMomPaid
        else compare this month to last month and set text and color
        */
        lifecycleScope.launch(Dispatchers.IO) {
            val today = Calendar.getInstance()
            val lastMonthBalance = db.userMonthBalanceDao().findByUserId(MainActivity.USER_ID, today.get(Calendar.YEAR), today.get(Calendar.MONTH))
            val thisMonthBalance = db.userMonthBalanceDao().findByUserId(MainActivity.USER_ID, today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1)!!
            if(lastMonthBalance == null) {
                withContext(Dispatchers.Main) {
                    binding.tvMomIncome.visibility = View.GONE
                    binding.tvMomPaid.visibility = View.GONE
                }
                return@launch
            }
            val incomeDiff = thisMonthBalance.incomeSummary.toDouble() / lastMonthBalance.incomeSummary * 100 - 100
            val paidDiff = thisMonthBalance.paidSummary.toDouble() / lastMonthBalance.paidSummary * 100 - 100
            var incomeDiffStr = String.format("%.2f%% (MoM)", incomeDiff)
            var paidDiffStr = String.format("%.2f%% (MoM)", paidDiff)
            var incomeDiffColor = ContextCompat.getColor(this@HistoryActivity, R.color.red)
            var paidDiffColor = ContextCompat.getColor(this@HistoryActivity, R.color.green)
            if(incomeDiff >= 0.0) {
                incomeDiffStr = "+$incomeDiffStr"
                incomeDiffColor = ContextCompat.getColor(this@HistoryActivity, R.color.green)
            }
            if(paidDiff >= 0.0) {
                paidDiffStr = "+$paidDiffStr"
                paidDiffColor = ContextCompat.getColor(this@HistoryActivity, R.color.red)
            }
            withContext(Dispatchers.Main) {
                binding.tvMomIncome.text = incomeDiffStr
                binding.tvMomIncome.setTextColor(incomeDiffColor)
                binding.tvMomPaid.text = paidDiffStr
                binding.tvMomPaid.setTextColor(paidDiffColor)
            }
        }
    }

    private fun initTimeRange() {
        // Init calendar
        firstDayOfMonth = Calendar.getInstance().also {
            it.set(it.get(Calendar.YEAR), it.get(Calendar.MONTH), 1, 0, 0, 0)
        }
        lastDayOfMonth = Calendar.getInstance().also {
            it.set(it.get(Calendar.YEAR), it.get(Calendar.MONTH), it.getActualMaximum(Calendar.DAY_OF_MONTH), 0, 0, 0)
        }
        // set text to tvTimeRange
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val timeRangeString = "${dateFormatter.format(firstDayOfMonth.time)} ~ ${dateFormatter.format(lastDayOfMonth.time)}"
        binding.tvTimeRange.text = timeRangeString
    }
}