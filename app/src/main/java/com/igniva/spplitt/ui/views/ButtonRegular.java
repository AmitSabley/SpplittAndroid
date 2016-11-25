package com.igniva.spplitt.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by igniva-php-08 on 25/5/16.
 */
public class ButtonRegular extends Button {

    public ButtonRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ButtonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonRegular(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface face = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/Ubuntu-R.ttf");
            setTypeface(face);
        }
    }
}
