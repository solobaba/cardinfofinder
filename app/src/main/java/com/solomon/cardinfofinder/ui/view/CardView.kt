package com.solomon.cardinfofinder.ui.view

import com.solomon.cardinfofinder.db.CardDetails

interface CardView {
    fun loadingFailed(msg: String)
    fun loadingSuccessful(msg: String)
    fun card (data : CardDetails?)
}