package com.jackmelvin.sothuchi.activity

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jackmelvin.sothuchi.databinding.ActivityInputBinding
import com.jackmelvin.sothuchi.dialog.CategoryDialogFragment
import com.jackmelvin.sothuchi.model.Category
import com.jackmelvin.sothuchi.model.Transaction
import java.text.SimpleDateFormat
import java.util.*


private lateinit var binding: ActivityInputBinding
class InputActivity : AppCompatActivity(), CategoryDialogFragment.OnCategorySelectedListener {
    private var chosenCategory: Category? = null
    private lateinit var chosenDate: Date

    companion object {
        val PAID_CATEGORY = "paidCategory"
        val INCOME_CATEGORY = "incomeCategory"
        val SELECTED_CATEGORY = "selectedCategory"
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
        val date = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        var dateString = sdf.format(date.time)
        binding.tvDate.text = dateString

        binding.tvDate.setOnClickListener { view  ->
            DatePickerDialog(
                this@InputActivity,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                    date.set(year, month, dayOfMonth)
                    dateString = sdf.format(date.time)
                    binding.tvDate.text = dateString
                },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        this.chosenDate = date.time
    }

    private fun initCompleteAction() {
        binding.tvComplete.setOnClickListener {
            println("Got here")
            if(binding.etAmount.text.toString().toInt() <= 0) {
                AlertDialog.Builder(this@InputActivity)
                    .setTitle("Verify")
                    .setMessage("Transaction amount must be greater than 0")
                    .setNegativeButton(R.string.ok, null)
                    .show()
            } else {
                val transaction = Transaction(
                    chosenDate,
                    binding.etAmount.text.toString().toInt(),
                    chosenCategory,
                    binding.etMemo.text.toString()
                )
                Toast.makeText(this@InputActivity, "Saved transaction" + transaction.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initCategorySelector() {
        binding.tvCategory.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(SELECTED_CATEGORY, if (binding.rbPaid.isChecked) PAID_CATEGORY else INCOME_CATEGORY)

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