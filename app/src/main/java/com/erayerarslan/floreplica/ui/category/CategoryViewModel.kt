package com.erayerarslan.floreplica.ui.category

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
class CategoryViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {
    private val _categoryList = MutableLiveData<List<String>>()
    val categoryList: LiveData<List<String>> get() = _categoryList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getCategoryList () {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getProductCategory()
                if (response.isSuccessful) {
                    _categoryList.value = response.body()
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            } catch (e: Exception) {

            }
            finally {
                _isLoading.value = false
            }
        }
    }
}