package com.igniva.spplitt.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by igniva-php-08 on 25/5/16.
 */
public class TextViewNonRegular extends TextView {

    public TextViewNonRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewNonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewNonRegular(Context context) {
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
