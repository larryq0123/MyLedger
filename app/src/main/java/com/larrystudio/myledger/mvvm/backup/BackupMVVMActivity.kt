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
    }

    private fun initListeners(){
        buttonBackup.setOnClickListener { viewModel.onBackupClicked() }
        buttonRestore.setOnClickListener {
            val json = editBackup.text.toString()
            viewModel.onRestoreClicked(json)
        }
    }
}