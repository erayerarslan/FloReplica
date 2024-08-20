package com.erayerarslan.floreplica.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.core.Response
import com.erayerarslan.floreplica.databinding.ProductHomeRecyclerviewBinding
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.model.User
import com.erayerarslan.floreplica.util.loadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProductAdapter(
    private val onProductClick: (ProductItem) -> Unit,
    private val onFavoriteClick: (ProductItem, Boolean) -> Unit
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private val productList = mutableListOf<ProductItem>()

    class ViewHolder(val binding: ProductHomeRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductHomeRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]



        holder.binding.apply {
            productName.text = product.title
            productPrice.text = product.price.toString()
            productImage.loadImage(product.image)

            root.setOnClickListener { product?.let { onProductClick(it) } }

            imgFavorite.setImageResource(if (product.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outlined)


            imgFavorite.setOnClickListener {
                if (product.isFavorite) {
                    holder.binding.imgFavorite.setImageResource(R.drawable.ic_heart_filled)
                    saveFavoriteStatus(product, false)
                    updateItem(position)

                } else {
                    holder.binding.imgFavorite.setImageResource(R.drawable.ic_heart_outlined)
                    saveFavoriteStatus(product, true)
                    updateItem(position)
                }
            }

            holder.itemView.setOnClickListener {
                onProductClick(product)
            }


        }

    }

    override fun getItemCount(): Int = productList.size

    fun updateProductList(newProductList: List<ProductItem>) {

        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()

    }
    fun updateItem(position: Int) {
        // Öncelikle, item'i güncelle
        val item = productList[position]
        item.isFavorite = !item.isFavorite
        // Daha sonra RecyclerView'ı güncelle
        notifyItemChanged(position)
    }


    private fun saveFavoriteStatus(product: ProductItem, isFavorite: Boolean) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .child("favorites")

        if (isFavorite) {
            userRef.child(product.id.toString()).setValue(true)
        } else {
            userRef.child(product.id.toString()).removeValue()
        }
    }

}
