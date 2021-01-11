package com.larrystudio.myledger.mvvm.backup

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvvm.BaseMVVMActivity
import com.larrystudio.myledger.mvvm.ViewModelFactory
import com.larrystudio.myledger.util.LogUtil
import kotlinx.android.synthetic.main.activity_backup_mvvm.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class BackupMVVMActivity : BaseMVVMActivity() {


    private lateinit var viewModel: BackupViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup_mvvm)

        initViewModel()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStartLifeCycle()
    }

    override fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(BackupViewModel::class.java)
        doBasicSubscription(viewModel)

        lifecycleScope.launch {
            LogUtil.logd(TAG, "thread name = ${Thread.currentThread().name}")
            val str1 = async {
                delay(1000)
                LogUtil.logd(TAG, "in async 1, thread name = ${Thread.currentThread().name}")
                "str1"
            }

            val str2 = async {
                delay(1800)
                LogUtil.logd(TAG, "in async 2, thread name = ${Thread.currentThread().name}")
                "str2"
            }

            val str = str1.await() + str2.await()
            LogUtil.logd(TAG, "after async block, str = $str, thread name = ${Thread.currentThread().name}")
        }
    }

    private fun initListeners(){
        buttonBackup.setOnClickListener { viewModel.onBackupClicked() }
        buttonRestore.setOnClickListener {  }
    }
}