package com.igniva.spplitt.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by igniva-php-08 on 25/5/16.
 */
public class RadioButtonNonRegular extends RadioButton {

    public RadioButtonNonRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RadioButtonNonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioButtonNonRegular(Context context) {
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
