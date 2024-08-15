package com.erayerarslan.floreplica.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.floreplica.databinding.ProductHomeRecyclerviewBinding
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.ui.home.ProductAdapter
import com.erayerarslan.floreplica.util.loadImage

class CategoryProductAdapter(private var productList: List<ProductItem?>,
                             private val onProductClick: (ProductItem) -> Unit
) : RecyclerView.Adapter<CategoryProductAdapter.ViewHolder>() {

    class ViewHolder(val binding: ProductHomeRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductHomeRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.apply {
            productName.text = product?.title
            productPrice.text = product?.price.toString()
            productImage.loadImage(product?.image)
            root.setOnClickListener { product?.let { onProductClick(it) } }
        }
    }

    fun updateProducts(newProductList: List<ProductItem?>) {
        productList = newProductList
        notifyDataSetChanged()
    }
}