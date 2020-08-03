package com.larry.larrylibrary.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import com.larry.larrylibrary.R;
import com.larry.larrylibrary.util.GlobalUtil;


public class ShadeButton extends CardView {

    private TextView textButton;
    private TextView textCover;



    private ShadeButtonListener listener;
    private int parentBackground;
    private int buttonBackground;
    private int penColor;
    private float penSize;


    private String text;
    private long timeActionDown;

    public ShadeButton(final Context context, AttributeSet attrs){
        super(context, attrs);

        getMyResources(context, attrs);
        generateView(getContext());
    }

    private void getMyResources(Context context, AttributeSet attrs){
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.shade_button, 0, 0);

        try {
            text = typedArray.getString(R.styleable.shade_button_text);
            parentBackground = typedArray.getResourceId(R.styleable.shade_button_parentBackground, GlobalUtil.getColor(context, R.color.white));
            buttonBackground = typedArray.getResourceId(R.styleable.shade_button_buttonBackground, GlobalUtil.getColor(context, R.color.white));
            penColor = typedArray.getColor(R.styleable.shade_button_penColor, GlobalUtil.getColor(context, R.color.black));
            penSize = typedArray.getDimension(R.styleable.shade_button_penSize, 15);
        } finally {
            typedArray.recycle();
        }
    }

    private void generateView(Context context){
        setBackground(null);

        textButton = new TextView(context);
        LayoutParams lpText = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textButton.setLayoutParams(lpText);
        textButton.setBackgroundResource(buttonBackground);
        textButton.setText(text);
        textButton.setTextColor(penColor);
        textButton.setTextSize(penSize);
        textButton.setGravity(Gravity.CENTER);
        addView(textButton);

        textCover = new TextView(context);
        LayoutParams lpCover = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textCover.setLayoutParams(lpCover);
        textCover.setBackgroundColor(Color.BLACK);
        textCover.setAlpha(0.35f);
        textCover.setVisibility(GONE);
        addView(textCover);
    }

    public void setShadeButtonListener(ShadeButtonListener l){listener = l;}

    public void setText(String text){
        textButton.setText(text);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                textCover.setVisibility(VISIBLE);
                timeActionDown = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                textCover.setVisibility(GONE);
                if(System.currentTimeMillis() - timeActionDown < 175 && listener != null){
//                    BaseLogUtil.logd("ShadeButton", "clicked");
                    listener.onButtonClicked(getId());
                }
                break;
        }

        return true;
    }

    public interface ShadeButtonListener{
        void onButtonClicked(int viewID);
    }
}
