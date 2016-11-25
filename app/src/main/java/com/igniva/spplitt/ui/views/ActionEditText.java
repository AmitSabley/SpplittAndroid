package com.igniva.spplitt.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

// An EditText that lets you use actions ("Done", "Go", etc.) on multi-line edits.
public class ActionEditText extends EditText
{
    public ActionEditText(Context context)
    {
        super(context);
        init();
    }

    public ActionEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ActionEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs)
    {
        InputConnection conn = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return conn;
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface face = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/Ubuntu-R.ttf");
            setTypeface(face);
        }
    }
}