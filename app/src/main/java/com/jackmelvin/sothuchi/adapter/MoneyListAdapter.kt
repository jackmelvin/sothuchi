package com.jackmelvin.sothuchi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jackmelvin.sothuchi.databinding.ItemMoneyBinding
import com.jackmelvin.sothuchi.model.MoneyWithCategory
import java.text.SimpleDateFormat
import java.util.*

class MoneyListAdapter(private val onItemClicked: (MoneyWithCategory) -> Unit) : ListAdapter<MoneyWithCategory, MoneyListAdapter.MoneyViewHolder>(DiffCallback) {
    class MoneyViewHolder(private val binding: ItemMoneyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(moneyWithCategory: MoneyWithCategory) {
            binding.ivMoneyCategory.setImageResource(moneyWithCategory.imageId)
            binding.tvAmount.text = moneyWithCategory.money.amount.toString()
            binding.tvDate.text = SimpleDateFormat("dd/MM", Locale.US).format(moneyWithCategory.money.date.time)
            binding.tvPlusMinus.text = if(moneyWithCategory.isIncome) "+" else "-"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyViewHolder {
        val viewHolder = MoneyViewHolder(
            ItemMoneyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MoneyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<MoneyWithCategory>() {
            override fun areItemsTheSame(oldItem: MoneyWithCategory, newItem: MoneyWithCategory): Boolean {
                return oldItem.money.id == newItem.money.id
            }

            override fun areContentsTheSame(oldItem: MoneyWithCategory, newItem: MoneyWithCategory): Boolean {
                return oldItem == newItem
            }
        }
    }
}