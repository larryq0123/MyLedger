package com.larrystudio.myledger.mvvm.splash

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvvm.BaseMVVMActivity
import com.larrystudio.myledger.mvvm.ViewModelFactory
import com.larrystudio.myledger.mvvm.main.MainMVVMActivity

class SplashMVVMActivity : BaseMVVMActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_mvvm)

        initViewModel()
        viewModel.onCreateLifeCycle()
    }

    override fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(SplashViewModel::class.java)
        doBasicSubscription(viewModel)
        viewModel.ldNavigate.observe(this, Observer {
            if(it == 1){ goPage(MainMVVMActivity::class.java, true) }
        })
    }
}