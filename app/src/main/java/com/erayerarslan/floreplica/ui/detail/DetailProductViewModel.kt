package com.erayerarslan.floreplica.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    private val _product = MutableLiveData<ProductItem>()
    val product: LiveData<ProductItem> = _product
    val isLoading = MutableLiveData(false)
    val errorMesssage : MutableLiveData<String?> = MutableLiveData()

    fun getProduct(productId: Int) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.apiService.getProduct(productId)
                _product.value = response
                println(response)
            } catch (e: Exception) {
                errorMesssage.value = e.message
                } finally {
                isLoading.value = false
            }
        }

    }

}