package com.larrystudio.myledger.mvp.main

import android.os.Bundle
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.relativeParent, MainFragment())
            .commit()
    }

}