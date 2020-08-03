package com.larry.larrylibrary.custom_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import com.larry.larrylibrary.util.BaseLogUtil;


public class BaseAnimationView extends AppCompatImageView {

    protected int[] drawableIDs;
    protected int index;
    private AnimationFinishListener listener;



    public BaseAnimationView(Context c){
        super(c);
    }

    public BaseAnimationView(Context c, AttributeSet attrs){
        super(c, attrs);
    }

    protected void setDrawableIDs(int[] ids){
        drawableIDs = ids;
        index = -1;
    }

    public void setAnimationFinishListener(AnimationFinishListener l){
        listener = l;
    }

    public void playNextDrawable(){
        if(drawableIDs == null || drawableIDs.length == 0){
            BaseLogUtil.logd(getClass().getSimpleName(), "drawable ID array is empty");
            return;
        }

        if(index < drawableIDs.length - 1){
            index++;
            setImageResource(drawableIDs[index]);
        }else if(listener == null){
            index = 0;
            setImageResource(drawableIDs[index]);
        }else{
            listener.onOneRoundFinished();
        }
    }

    public void resetIndex(){
        index = -1;
    }

    public interface AnimationFinishListener {
        void onOneRoundFinished();
    }
}
