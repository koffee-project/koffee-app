package eu.yeger.koffee.utility

import android.os.CountDownTimer

/**
 * Utility class for abstracting starting and restarting of count down timers with a single tick.
 *
 * @property onCompletion Called upon completion of a tick.
 *
 * @author Jan Müller
 */
class SimpleTimer(private val onCompletion: () -> Unit) {

    private var countDownTimer: CountDownTimer? = null

    /**
     * Starts the timer, which will perform a single tick after [duration] milliseconds.
     * Cancels the previously active timer.
     *
     * @param duration The duration of the timer.
     */
    fun start(duration: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(duration, duration) {
            override fun onFinish() = onCompletion()
            override fun onTick(millisUntilFinished: Long) = Unit
        }.start()
    }

    /**
     * Stops the timer.
     */
    fun stop() {
        countDownTimer?.cancel()
        countDownTimer = null
    }
}
