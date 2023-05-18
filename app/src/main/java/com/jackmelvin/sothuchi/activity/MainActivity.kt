package com.jackmelvin.sothuchi.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.database.AppDatabase
import com.jackmelvin.sothuchi.databinding.ActivityMainBinding
import com.jackmelvin.sothuchi.model.UserMonthBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var db: AppDatabase? = null
    private val calendar = Calendar.getInstance()
    private var userMonthBalance: UserMonthBalance? = null
    private var incomeSummary = 0
    private var paidSummary = 0
    private var balanceSummary = 0

    companion object {
        val USER_ID: Long = 1L
        val INCOME_SUMMARY = "incomeSummary"
        val PAID_SUMMARY = "paidSummary"
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

    private fun setButtonAction() {
        binding.clInput.setOnClickListener {
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            startActivity(intent)
        }
        binding.btnHistory.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoryActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(INCOME_SUMMARY, incomeSummary)
            bundle.putInt(PAID_SUMMARY, paidSummary)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun initDBRelatedViews() {
        lifecycleScope.launch(Dispatchers.IO) {
            db = AppDatabase.getInstance(this@MainActivity)
            userMonthBalance = db?.userMonthBalanceDao()?.findByUserId(USER_ID, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
            if (userMonthBalance == null) {
                userMonthBalance = UserMonthBalance(USER_ID, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0, 0)
                db?.userMonthBalanceDao()?.insert(userMonthBalance!!)
            }
            userMonthBalance?.let {
                incomeSummary = it.incomeSummary
                paidSummary = it.paidSummary
                balanceSummary = it.incomeSummary - it.paidSummary
            }
            withContext(Dispatchers.Main) {
                binding.tvPaid.text = paidSummary.toString()
                binding.tvIncome.text = incomeSummary.toString()
                binding.tvBalance.text = balanceSummary.toString()
            }
        }
    }

    private fun setDate() {
        val dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        val dateString = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)+1} ($dayOfWeek)"
        binding.tvDate.text = dateString
    }

    private fun getDayOfWeek(i: Int): String {
        return resources.getStringArray(R.array.dayOfWeek)[i-1]
    }
}