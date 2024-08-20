package com.erayerarslan.floreplica.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.floreplica.databinding.CategoryRecyclerviewBinding
import com.erayerarslan.floreplica.databinding.ProductHomeRecyclerviewBinding
import kotlinx.coroutines.flow.MutableStateFlow

class CategoryAdapter(private var categories: List<String>,
                      private val onCategoryClick: (String) -> Unit
    ) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: CategoryRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CategoryRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return categories.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.textViewCategoryName.text = category
        holder.itemView.setOnClickListener {
            onCategoryClick(category)
        }
    }
    fun updateCategories(newCategories: List<String>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
