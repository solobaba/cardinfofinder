package com.solomon.cardinfofinder.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cards.pay.paycardsrecognizer.sdk.Card
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent.CancelReason
import com.solomon.cardinfofinder.MainApplication
import com.solomon.cardinfofinder.R
import com.solomon.cardinfofinder.ui.base.BaseActivity
import com.solomon.cardinfofinder.ui.viewModel.MainViewModel
import com.solomon.cardinfofinder.utils.getCartLogo
import com.solomon.cardinfofinder.utils.getGreetingIcon
import com.solomon.cardinfofinder.utils.greetings
import com.solomon.cardinfofinder.utils.setCardNumber
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card.*

class MainActivity : BaseActivity() {
    private var viewModel: MainViewModel? = null
    val REQUEST_CODE_SCAN_CARD = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view()
    }

    //To keep the oncreate page clean
    fun view() {
        CardNumberListner()

        //Instantiate view model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        btn.setOnClickListener {
            scanCard()
        }

        //Handled in utils
        getGreetingIcon()?.let { icon_img.setImageResource(it) }

        //Handled in utils
        greeting_.text = greetings()

        viewModel?._error?.observe(this, Observer {
            toastError(it)
            progressDialog(false)
        })

        viewModel?._sucessfful?.observe(this, Observer {
            toastSuccess(it)
            progressDialog(false)
        })

        //To observe result from server and send through intent to display page
        viewModel?._card?.observe(this, Observer {
            if (it != null) {
                val i = Intent(
                    MainApplication.instance?.applicationContext,
                    CardDetailActivity::class.java
                )
                i.putExtra("data", it)
                startActivity(i)
            }
            progressDialog(false)
        })
    }

    //Card number edit text listner
    private fun CardNumberListner() {
        edtCardNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) { // Remove all spacing char

                //To append logo to card ui
                if (s.isNotEmpty()) {
                    providerLogo.visibility = View.VISIBLE
                    //handled in utils to set card logo
                    providerLogo.setImageResource(getCartLogo(s))
                } else {
                    providerLogo.visibility = View.GONE
                }

                //Logic to space card number
                setCardNumber(s)
                if (s.isNotEmpty()) {
                    tv_card_number.text = s.toString()
                } else {
                    tv_card_number.text = getString(R.string.card_number)
                }

                //Get card details from server when edit text completed
                postCardDetailsToServer(s)
            }
        })
    }

    //To call the ocr scan
    private fun scanCard() {
        val intent: Intent = ScanCardIntent.Builder(this).build()
        startActivityForResult(intent, REQUEST_CODE_SCAN_CARD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCAN_CARD) {
            if (resultCode == Activity.RESULT_OK) {
                val card: Card? = data?.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD)

                setCard(card)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                @ScanCardIntent.CancelReason val reason: Int = data?.getIntExtra(
                    ScanCardIntent.RESULT_CANCEL_REASON,
                    ScanCardIntent.BACK_PRESSED
                )
                    ?: ScanCardIntent.BACK_PRESSED

            } else if (resultCode == ScanCardIntent.RESULT_CODE_ERROR) {
                //Log.i(cards.pay.sample.demo.CardDetailsActivity.TAG, "Scan failed")
            }
        }
    }

    //Result from ocr and send to view model to post to the server
    private fun setCard(card: Card?) {
        if (card != null) {
            //show user progress bar before posting to the server
            progressDialog(true)
            viewModel?.postData(card.cardNumber.toString())
        }
    }

    private fun postCardDetailsToServer(s: Editable) {
        if (s.length == 19) {
            val k: String = tv_card_number.text.toString().replace(" ", "")

            //Show user progress bar before posting to the server
            progressDialog(true)
            viewModel?.postData(k)
        }
    }

    fun progressDialog(bol: Boolean) {
        if (bol) {
            frame_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
            progress_bar.visibility = View.VISIBLE
            btn.isEnabled = false
            edtCardNumber.isEnabled = false

        } else {
            frame_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            progress_bar.visibility = View.GONE
            btn.isEnabled = true
            edtCardNumber.isEnabled = true
            edtCardNumber.text.clear()
        }
    }
}
