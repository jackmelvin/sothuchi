package com.jackmelvin.sothuchi.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jackmelvin.sothuchi.database.AppDatabase
import com.jackmelvin.sothuchi.databinding.ActivityInputBinding
import com.jackmelvin.sothuchi.dialog.CategoryDialogFragment
import com.jackmelvin.sothuchi.model.Category
import com.jackmelvin.sothuchi.model.Money
import com.jackmelvin.sothuchi.model.UserMonthBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InputActivity : AppCompatActivity(), CategoryDialogFragment.OnCategorySelectedListener {
    private lateinit var binding: ActivityInputBinding
    private var chosenCategory: Category? = null
    private lateinit var chosenDate: Calendar

    companion object {
        val IS_INCOME = "isIncome"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        initBackAction()
        initDatePicker()
        initCategorySelector()
        initCompleteAction()
    }

    private fun initDatePicker() {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        var dateString = sdf.format(calendar.time)
        binding.tvDate.text = dateString

        binding.tvDate.setOnClickListener {
            DatePickerDialog(
                this@InputActivity,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    dateString = sdf.format(calendar.time)
                    binding.tvDate.text = dateString
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        this.chosenDate = calendar
    }

    private fun initCompleteAction() {
        binding.tvComplete.setOnClickListener {
            if (binding.etAmount.text.isBlank() || binding.etAmount.text.toString().toInt() <= 0) {
                AlertDialog.Builder(this@InputActivity)
                    .setTitle("Verify")
                    .setMessage("Amount must be greater than 0")
                    .setNegativeButton(android.R.string.ok, null)
                    .show()
            } else if(chosenCategory == null) {
                AlertDialog.Builder(this@InputActivity)
                    .setTitle("Verify")
                    .setMessage("Category must be selected")
                    .setNegativeButton(android.R.string.ok, null)
                    .show()
            } else {
                chosenCategory?.let {
                    val money = Money(
                        chosenDate,
                        binding.etAmount.text.toString().toInt(),
                        it.id,
                        MainActivity.USER_ID,
                        binding.etMemo.text.toString()
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        val db = AppDatabase.getInstance(this@InputActivity)
                        db.moneyDao().insert(money)
                        // Update user's balance
                        var userMonthBalance: UserMonthBalance? = db.userMonthBalanceDao().findByUserId(MainActivity.USER_ID, money.date.get(Calendar.YEAR), money.date.get(Calendar.MONTH) + 1)
                        userMonthBalance = userMonthBalance ?: UserMonthBalance(MainActivity.USER_ID, money.date.get(Calendar.YEAR), money.date.get(Calendar.MONTH) + 1, 0, 0)
                        if(it.isIncome) {
                            userMonthBalance.incomeSummary += money.amount
                        } else {
                            userMonthBalance.paidSummary += money.amount
                        }
                        db.userMonthBalanceDao().insert(userMonthBalance)
                    }
                    startActivity(Intent(this@InputActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun initCategorySelector() {
        binding.tvCategory.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(IS_INCOME, binding.rbIncome.isChecked)

            val dialogFragment = CategoryDialogFragment(this)
            dialogFragment.arguments = bundle
            dialogFragment.show(supportFragmentManager, "Category")
        }
    }

    override fun chooseCategory(category: Category) {
        binding.tvCategory.text = category.name
        binding.tvCategory.setTextColor(binding.etAmount.textColors)
        this.chosenCategory = category
    }

    private fun initBackAction() {
        binding.tvBack.setOnClickListener {
            val intent = Intent(this@InputActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}