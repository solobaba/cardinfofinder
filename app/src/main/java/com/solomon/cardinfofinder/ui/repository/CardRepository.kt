package com.solomon.cardinfofinder.ui.repository

import com.solomon.cardinfofinder.api.RetrofitClient
import com.solomon.cardinfofinder.db.CardDetails
import com.solomon.cardinfofinder.ui.view.CardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardRepository {
    fun getCardDetails(card_num: String, callback: CardView) {

        RetrofitClient.getInstance()?.getApi()?.getCard(card_num)?.enqueue(object :
            Callback<CardDetails> {
            override fun onFailure(call: Call<CardDetails>, t: Throwable) {
                callback.loadingFailed(t.toString())
            }

            override fun onResponse(call: Call<CardDetails>, response: Response<CardDetails>) {

                if (response.isSuccessful && response.body() != null) {
                    when (response.code()) {
                        200 -> {
                            response.body()?.let {
                                callback.card(it)
                            }

                            callback.loadingSuccessful("valid card")
                        }

                        else -> {
                            callback.loadingFailed("Invalid request.. please try again")
                        }
                    }
                } else {
                    callback.loadingFailed("Invalid request..Check your card details and try again")
                }
            }
        })
    }
}