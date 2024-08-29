package com.erayerarslan.floreplica.ui.home.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.ProductDetailTopRecyclerviewBinding
import com.erayerarslan.floreplica.databinding.ProductHomeRecyclerviewBinding
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.util.DetailImage
import com.erayerarslan.floreplica.util.loadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class ProductAdapter (
    private val onProductClick: (ProductItem) -> Unit,
    private val isDetailPage: Boolean =false,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) :
    RecyclerView.Adapter<ViewHolder>() {
    private val productList = mutableListOf<ProductItem>()

    companion object {
        private const val TYPE_HOME = 0
        private const val TYPE_DETAIL = 1
    }
    override fun getItemViewType(position: Int): Int {
        return if (isDetailPage) TYPE_DETAIL else TYPE_HOME
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_HOME -> {
                TempViewHolder(
                    ProductHomeRecyclerviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_DETAIL -> {
                TempViewHolder2(
                    ProductDetailTopRecyclerviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                TempViewHolder2(
                    ProductDetailTopRecyclerviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    inner class TempViewHolder internal constructor(val binding: ProductHomeRecyclerviewBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(product: ProductItem){
            val inflater = LayoutInflater.from(this.itemView.context)
            val popupView = inflater.inflate(R.layout.popup_layout, null)
            val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

            binding.apply {
                productName.text = product.title
                productPrice.text = product.price.toString()
                productImage.loadImage(product.image)

                root.setOnClickListener { product?.let { onProductClick(it) } }

                imgFavorite.setImageResource(
                    if (product.isFavorite) {
                        R.drawable.ic_heart_filled
                    }
                    else {
                        R.drawable.ic_heart_outlined
                    }
                )

                imgFavorite.setOnClickListener {
                    if (product.isFavorite) {
                            binding.imgFavorite.setImageResource(R.drawable.ic_heart_outlined)
                            saveFavoriteStatus(product, false)
                            updateItem(bindingAdapterPosition)
                    } else {
                        if(auth.currentUser?.isAnonymous == true){

                            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)

                            val closeButton = popupView.findViewById<Button>(R.id.popup_close)
                            closeButton.setOnClickListener {
                                popupWindow.dismiss()
                            }
                            val loginButton = popupView.findViewById<Button>(R.id.popup_login)
                            loginButton.setOnClickListener {
                                findNavController(binding.root.findFragment()).navigate(R.id.signInFragment)
                                popupWindow.dismiss()
                            }

                        }
                        else{
                            binding.imgFavorite.setImageResource(R.drawable.ic_heart_outlined)
                            saveFavoriteStatus(product, true)
                            updateItem(bindingAdapterPosition)
                        }

                    }
                }

                itemView.setOnClickListener {
                    onProductClick(product)
                }
            }

        }
    }
    inner class TempViewHolder2 internal constructor(val binding: ProductDetailTopRecyclerviewBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(product: ProductItem){
            binding.apply {
                textViewDetailTitle.text = product.title
                imageViewProduct.DetailImage(product.image)
                textRate.text = "Rating: " + product.rating?.rate.toString()
                ratingBar.rating = product.rating?.rate.toString().toFloat()
                textViewDetailDescription.text = product.description
                similarProduct.visibility = ViewGroup.VISIBLE


            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        when (holder) {
            is TempViewHolder -> holder.bind(product)
            is TempViewHolder2 -> holder.bind(product)
        }

    }

    override fun getItemCount(): Int = productList.size

    fun updateProductList(newProductList: List<ProductItem>) {

        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()

    }
    fun updateProductDetail(product: ProductItem) {
        productList.clear()
        productList.add(product)
        notifyDataSetChanged()
        val position = productList.let {
            it.indexOfFirst { it.id == product.id }
        }

            productList[position] = product

    }
    fun updateItem(position: Int) {
        //  item'i güncelle
        val item = productList[position]
        item.isFavorite = !item.isFavorite
        // RecyclerView'ı güncelle
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
