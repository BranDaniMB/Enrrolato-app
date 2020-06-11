package com.enrrolato.enrrolato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics

public class PopupConfirmExit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_confirm_exit)

        val metric: DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)

        val widht : Int = metric.widthPixels
        val height: Int = metric.heightPixels

        window.setLayout(((widht * 0.5).toInt()),  (height * 0.5).toInt())
    }
}