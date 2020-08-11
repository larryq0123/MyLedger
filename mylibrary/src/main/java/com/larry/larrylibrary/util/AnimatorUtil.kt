package com.larry.larrylibrary.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import com.larry.larrylibrary.R
import com.larry.larrylibrary.thread.ExecutorPool

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

    fun animateBackgroundDim(backgroundLayout: ViewGroup, reverse: Boolean) {
        val transColor = if (reverse) R.color.blackTrans else R.color.transparent
        val blackTransColor = if (reverse) R.color.transparent else R.color.blackTrans
        val anim = ValueAnimator()
        anim.setIntValues(transColor, blackTransColor)
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator -> backgroundLayout.setBackgroundColor((valueAnimator.animatedValue as Int)) }
        anim.duration = SLIDE_ANIMATION_DURATION
        anim.start()
    }

    interface SlideAnimationCallback { fun onAnimationEnd() }
}