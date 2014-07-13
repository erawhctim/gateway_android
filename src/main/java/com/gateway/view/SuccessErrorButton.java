package com.gateway.view;

import android.content.Context;
import android.util.AttributeSet;

import com.dd.CircularProgressButton;

/**
 * Represents a loading button with "indeterminate" progress,
 * with separate loading, success, and error states.
 */
public class SuccessErrorButton extends CircularProgressButton {

    public SuccessErrorButton(Context context) {
        super(context);
        this.setIndeterminateProgressMode(true);
    }

    public SuccessErrorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setIndeterminateProgressMode(true);
    }

    public SuccessErrorButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setIndeterminateProgressMode(true);
    }

    public void showError() {
        this.setProgress(-1);
    }

    public void showSuccess() {
        this.setProgress(100);
    }

    public void showLoading() {
        this.setProgress(50);
    }

    public void showIdle() {
        this.setProgress(0);
    }
}
