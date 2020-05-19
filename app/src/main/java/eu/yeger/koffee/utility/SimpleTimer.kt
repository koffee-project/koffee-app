package eu.yeger.koffee.utility

import android.os.CountDownTimer

class SimpleTimer(private val onCompletion: () -> Unit) {

    private var countDownTimer: CountDownTimer? = null

    fun start(duration: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(duration, duration) {
            override fun onFinish() = onCompletion()
            override fun onTick(millisUntilFinished: Long) = Unit
        }.start()
    }

    fun stop() {
        countDownTimer?.cancel()
        countDownTimer = null
    }
}
