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
    private val userRepository: UserRepositoryImpl,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

) : ViewModel() {
    val favoriteProducts = MutableLiveData<List<ProductItem>>()
    private val _emptyFavorite = MutableLiveData<Boolean>()
    val emptyFavorite: LiveData<Boolean> get() = _emptyFavorite
    val guestUser = MutableLiveData<Boolean>()

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
                if (favoriteProducts.value.isNullOrEmpty()) {
                    _emptyFavorite.value = true
                    if(auth.currentUser?.isAnonymous == true){
                        guestUser.value = true
                    }
                    else{
                        guestUser.value = false
                    }


                }else{
                    _emptyFavorite.value = false
                }

            }catch (e: Exception){

            }
        }
    }
}