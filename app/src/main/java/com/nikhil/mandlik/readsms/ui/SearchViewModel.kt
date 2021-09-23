package com.nikhil.mandlik.readsms.ui

import android.content.ContentResolver
import android.provider.Telephony
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.mandlik.readsms.entity.SMS
import com.nikhil.mandlik.readsms.repository.SmsRepository
import com.nikhil.mandlik.readsms.utility.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(private val smsRepository: SmsRepository) : ViewModel() {

    private var _smsList: MutableLiveData<Response<List<SMS>>> = MutableLiveData()
    val smsList: LiveData<Response<List<SMS>>> get() = _smsList


    fun searchSms(contentResolver: ContentResolver?, phoneNumber: String, noOfDays: Int) {
        viewModelScope.launch {
            try{
                _smsList.postValue(Response.Loading())
                _smsList.postValue(Response.Success(smsRepository.search(contentResolver,phoneNumber,noOfDays)))

            }catch (exception : Exception){
                _smsList.postValue(Response.Error(exception.message,exception))
            }
        }
    }

}