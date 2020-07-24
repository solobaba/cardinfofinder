package com.solomon.cardinfofinder.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solomon.cardinfofinder.db.CardDetails
import com.solomon.cardinfofinder.ui.repository.CardRepository
import com.solomon.cardinfofinder.ui.view.CardView

class MainViewModel : ViewModel(), CardView {
    var presenter: CardRepository = CardRepository()

    //Get success message from the server
    var _sucessfful= MutableLiveData<String>()

    //Get error message from the server
    var _error= MutableLiveData<String>()

    //Get card details  from the server
    var _card= MutableLiveData<CardDetails>()

    //Send card to server
    fun postData(card_num : String){
        presenter.getCardDetails(card_num,this)
    }

    override fun loadingFailed(msg: String) {
        _error.postValue(msg)
    }

    override fun loadingSuccessful(msg: String) {
        _sucessfful.postValue(msg)
    }

    override fun card(data: CardDetails?) {
        _card.postValue(data)
    }
}