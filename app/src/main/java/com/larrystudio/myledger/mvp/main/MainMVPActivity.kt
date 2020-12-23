package com.larrystudio.myledger.mvp.main

import android.os.Bundle
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvp.BaseMVPActivity

class MainMVPActivity : BaseMVPActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.relativeParent, MainMVPFragment())
            .commit()
    }

}