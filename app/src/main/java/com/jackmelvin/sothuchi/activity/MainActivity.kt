package com.jackmelvin.sothuchi.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.databinding.ActivityMainBinding
import java.util.*

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    var balance = 0
    var income = 0
    var paid = 0
    var currency = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        currency = resources.getString(R.string.currency_yen)
        setDate()
        setBalance()
        setIncome()
        setPaid()
        setInputAction()
    }

    private fun setInputAction() {
        binding.clInput.setOnClickListener {
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setPaid() {
        val paidString = currency + " " + paid
        binding.tvPaid.text = paidString
    }

    private fun setIncome() {
        val incomeString = currency + " " + income
        binding.tvIncome.text = incomeString
    }

    private fun setBalance() {
        val balanceString = currency + " " + balance
        binding.tvBalance.text = balanceString
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        val dateString = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)+1} ($dayOfWeek)"
        binding.tvDate.text = dateString
    }

    private fun getDayOfWeek(i: Int): String {
        return resources.getStringArray(R.array.dayOfWeek)[i-1]
    }
}