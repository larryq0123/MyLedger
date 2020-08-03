package com.larry.larrylibrary.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.larry.larrylibrary.thread.ExecutorPool
import java.lang.Exception

object AnimatorUtil {

    const val SLIDE_ANIMATION_DURATION = 300L

    @Volatile
    private var clickAllowed = true

    fun isClickAllowed(timeInMillis: Long = 150L): Boolean{
        if(clickAllowed){
            clickAllowed = false
            ExecutorPool.getInstance().forLightWeightBackgroundTasks().execute {
                try{
                    Thread.sleep(timeInMillis)
                }catch (e: Exception){
                    e.printStackTrace()
                }

                clickAllowed = true
            }

            return true
        }

        return false
    }

    fun slideVertically(target: ViewGroup, startPosition: Float, endPosition: Float, listener: SlideAnimationCallback?) {
        target.translationY = startPosition
        target.animate()
            .translationY(endPosition)
            .setDuration(SLIDE_ANIMATION_DURATION)
            .setInterpolator(AccelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    listener?.onAnimationEnd()
                }
            })
    }

    interface SlideAnimationCallback { fun onAnimationEnd() }
}