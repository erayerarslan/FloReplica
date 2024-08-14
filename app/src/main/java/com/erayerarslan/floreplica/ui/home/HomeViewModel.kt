package com.erayerarslan.floreplica.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    private val _productList = MutableLiveData<List<ProductItem>>()
    val productList: LiveData<List<ProductItem>> get() = _productList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getProductList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getProductList()
                if (response.isSuccessful) {
                    _productList.value = response.body()
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            } catch (e: HttpException) {
                _errorMessage.value = "HttpException: ${e.message()}"
            } catch (e: IOException) {
                _errorMessage.value = "IOException: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}