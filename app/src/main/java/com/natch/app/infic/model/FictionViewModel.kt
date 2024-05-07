package com.natch.app.infic.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FictionViewModel: ViewModel() {
    val currentFiction: MutableLiveData<Fiction> = MutableLiveData()
}