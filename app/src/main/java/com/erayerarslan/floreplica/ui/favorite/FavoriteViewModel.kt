package com.erayerarslan.floreplica.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.repository.ProductRepository
import com.erayerarslan.floreplica.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: ProductRepository,
    private val userRepository: UserRepositoryImpl
) : ViewModel() {
    val favoriteProducts = MutableLiveData<List<ProductItem>>()

    fun getFavoriteProductList() {
        viewModelScope.launch {

            try {

                val favoriteIds = userRepository.getFavoriteProductIds()
                // ürünleri uidye göre al
                val products = repository.getProductsByIds(favoriteIds)
                products.forEach { product ->
                    product.isFavorite = true
                }
                // livedataya yolla
                favoriteProducts.value = products

            }catch (e: Exception){

            }
        }
    }
}