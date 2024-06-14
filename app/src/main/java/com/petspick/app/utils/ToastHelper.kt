package com.petspick.app.utils

import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast

class ToastHelper {

    companion object {
        var toast: Toast? = null
        var lastMessage: String = ""
    }

    fun show(context: Context, text: String) {
        if (text == lastMessage) return

        toast?.cancel()
        toast= Toast.makeText(context, text, Toast.LENGTH_LONG)
        lastMessage = text
        toast?.show()

        val timer = object: CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) { }

            override fun onFinish() {
                lastMessage = ""
            }
        }
        timer.start()
    }
}