package com.erayerarslan.floreplica.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.network.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val productList : MutableLiveData<List<ProductItem?>?> = MutableLiveData()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData(false)
    val errorMessage : MutableLiveData<String?> = MutableLiveData()

    fun getProductList(){
        isLoading.value = true

        viewModelScope.launch {
            try {
                val response = ApiClient.getClient().getProductList()
                if (response.isSuccessful){
                    productList.postValue(response.body()?.productItems)

                }else{
                    if (productList.value.isNullOrEmpty()){
                        errorMessage.value = "No data found"
                    }
                    else{
                        errorMessage.value= response.message()
                    }

                }
            } catch (e: Exception) {
                    errorMessage.value=e.message
            }
            finally {
                isLoading.value=true
            }

        }
    }
}