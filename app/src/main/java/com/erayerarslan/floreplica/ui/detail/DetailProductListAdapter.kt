package com.erayerarslan.floreplica.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.ProductDetailRecyclerviewBinding
import com.erayerarslan.floreplica.databinding.ProductDetailTopRecyclerviewBinding
import com.erayerarslan.floreplica.databinding.ProductHomeRecyclerviewBinding
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.util.detailImage
import com.erayerarslan.floreplica.util.loadImage

class DetailProductListAdapter(
    val onProductClick: (ProductItem) -> Unit


) : RecyclerView.Adapter<DetailProductListAdapter.ViewHolder>() {
    private val productList = mutableListOf<ProductItem>()

    class ViewHolder(val binding: ProductDetailRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolder2(val binding: ProductDetailTopRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductDetailRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = productList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.binding.apply {
            productName.text = product.title
            productImage.detailImage(product.image)

            holder.itemView.setOnClickListener {
                onProductClick(product)
            }
        }

    }

    fun updateProductList(newProductList: List<ProductItem>) {

        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()

    }
}
