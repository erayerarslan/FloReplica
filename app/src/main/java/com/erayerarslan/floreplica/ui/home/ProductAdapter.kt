package com.erayerarslan.floreplica.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.floreplica.databinding.ProductHomeRecyclerviewBinding
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.util.loadImage

class ProductAdapter (private val onProductClick: (ProductItem) -> Unit):
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var productList: MutableList<ProductItem> = mutableListOf()

    class ViewHolder(val binding: ProductHomeRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ProductHomeRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.apply {
            productName.text = product.title
            productPrice.text = product.price.toString()
            productImage.loadImage(product.image)
            root.setOnClickListener { product?.let { onProductClick(it) } }
        }
    }

    override fun getItemCount(): Int = productList.size

    fun updateProductList(newProductList: List<ProductItem>) {
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()
    }
}
