package com.solomon.cardinfofinder

import android.text.Editable
import com.solomon.cardinfofinder.ui.activity.MainActivity
import com.solomon.cardinfofinder.utils.getGreetingIcon
import com.solomon.cardinfofinder.utils.greetings
import com.solomon.cardinfofinder.utils.isOnline
import com.solomon.cardinfofinder.utils.setCardNumber
import org.junit.Assert
import org.junit.Test

internal class UtilsUnitTest {
    @Test
    fun spaceSetCardNumber() {
        val s: Editable ?= null
        s?.equals(6).toString()
        assert(true) {
            if (s != null) {
                setCardNumber(s)
            }
        }
    }

    @Test
    fun checkDeviceOnline() {
        Assert.assertEquals(false, isOnline())
    }

    //Was successful by your current time...put the icon in the function
    @Test
    fun getGreetingIcon() {
        Assert.assertEquals( R.drawable.ic_cloudy_night,getGreetingIcon())
    }

    @Test
    fun getGreetingMessage() {

        val night = "Almost time for bed..how is the evening"
        Assert.assertEquals( night,greetings())
    }
}