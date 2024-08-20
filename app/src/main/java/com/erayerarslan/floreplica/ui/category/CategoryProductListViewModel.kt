package com.erayerarslan.floreplica.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.repository.ProductRepository
import com.erayerarslan.floreplica.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductListViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val userRepository: UserRepositoryImpl


) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    val productList: MutableLiveData<List<ProductItem>> = MutableLiveData()

    fun getProductListForCategory(category: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val allProducts = repository.getAllProducts()
                val filteredProducts = allProducts.filter { it.category == category }
                // Favori ürünleri kontrol et
                val favoriteIds = userRepository.getFavoriteProductIds()
                allProducts.map { product ->
                    product.isFavorite = favoriteIds.contains(product.id.toString())
                }

                productList.postValue(filteredProducts)
            } catch (e: Exception) {
                // Hata yönetimi
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}
