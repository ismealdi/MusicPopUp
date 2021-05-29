package com.techpopup.musicpopup.application.base

import android.content.Intent
import com.techpopup.musicpopup.R
import com.techpopup.musicpopup.ui.music.MusicActivity
import java.util.*
import kotlin.concurrent.timerTask

class AmSplash : AmActivity(R.layout.view_splash) {

    private var timerAnimate = Timer()
    private var countAnimate = 0

    override fun view() {
        super.view()
        animate()
    }

    private fun animate() {
        timerAnimate = Timer()
        timerAnimate.scheduleAtFixedRate(timerTask {
            countAnimate++

            if (countAnimate > 6) {
                load()
                timerAnimate.cancel()
                finish()
            }

        }, 0, 500)
    }

    private fun load() {
        runOnUiThread {
            startActivity(Intent(this, MusicActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

}