package com.jackmelvin.sothuchi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.database.AppDatabase
import com.jackmelvin.sothuchi.model.Money
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MoneyAdapter(private val moneyList: List<Money>): RecyclerView.Adapter<MoneyAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView
        val tvDate: TextView
        val tvAmount: TextView
        val tvPlusMinus: TextView
        init {
            image = view.findViewById(R.id.ivMoneyCategory)
            tvDate = view.findViewById(R.id.tvDate)
            tvAmount = view.findViewById(R.id.tvAmount)
            tvPlusMinus = view.findViewById(R.id.tvPlusMinus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val db = AppDatabase.getInstance(holder.itemView.context)
        GlobalScope.launch(Dispatchers.IO) {
            val category = db.categoryDao().findById(moneyList[position].categoryId)
            category?.let {
                withContext(Dispatchers.Main) {
                    holder.image.setImageResource(it.imageId)
                    val dateFormatter = SimpleDateFormat("dd/MM", Locale.US)
                    holder.tvDate.text = dateFormatter.format(moneyList[position].date.time)
                    holder.tvAmount.text = moneyList[position].amount.toString()
                    holder.tvPlusMinus.text = if(it.isIncome) "+" else "-"
                }
            }
        }
    }

    override fun getItemCount(): Int = moneyList.size
}