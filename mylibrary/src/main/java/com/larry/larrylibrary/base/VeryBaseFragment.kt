package com.larry.larrylibrary.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.larry.larrylibrary.util.BaseLogUtil

/**
 * Created by user on 2018/9/5.
 */
abstract class VeryBaseFragment: Fragment() {
    protected val TAG: String = javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        BaseLogUtil.logLifeCycle(TAG, "onAttach")
    }

    override fun onPause() {
        super.onPause()
        BaseLogUtil.logLifeCycle(TAG, "onPause")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BaseLogUtil.logLifeCycle(TAG, "onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        BaseLogUtil.logLifeCycle(TAG, "onActivityCreated")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseLogUtil.logLifeCycle(TAG, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        BaseLogUtil.logLifeCycle(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        BaseLogUtil.logLifeCycle(TAG, "onResume")
    }

    override fun onDetach() {
        super.onDetach()
        BaseLogUtil.logLifeCycle(TAG, "onDetach")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        BaseLogUtil.logLifeCycle(TAG, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BaseLogUtil.logLifeCycle(TAG, "onDestroyView")
    }

    override fun onStop() {
        super.onStop()
        BaseLogUtil.logLifeCycle(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        BaseLogUtil.logLifeCycle(TAG, "onDestroy")
    }
}