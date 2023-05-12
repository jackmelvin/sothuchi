package com.jackmelvin.sothuchi.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.model.Category

class CategoryAdapter(private val onItemClickListener: OnItemClickListener, private val categoryList: List<Category>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ivCategory: ImageView
        val tvCategoryName: TextView

        init {
            ivCategory = view.findViewById(R.id.ivCategory)
            tvCategoryName = view.findViewById(R.id.tvCategoryName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivCategory.setImageResource(categoryList[position].image)
        holder.tvCategoryName.text = categoryList[position].name
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemCount() = categoryList.size
}