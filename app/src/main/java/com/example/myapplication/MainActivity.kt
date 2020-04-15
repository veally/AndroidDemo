package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sv_main_score.setData(5f, 10f)
        dsv_score.setText("Hello", 20, 20, true)
//        dsv_score2.setText("你说呢2", 50, 100, true)
    }
}
