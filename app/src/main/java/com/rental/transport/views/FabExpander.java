package com.rental.transport.views;

import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FabExpander {

    private Animation showAnimation;
    private Animation hideAnimation;

    private Double width;
    private Double height;

    public FloatingActionButton fab;

    public FabExpander(FloatingActionButton fab,
                       Animation show,
                       Animation hide,
                       Double width,
                       Double height) {

        this.fab = fab;
        this.showAnimation = show;
        this.hideAnimation = hide;
        this.width = width;
        this.height = height;
    }

    public void expand() {

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fab.getLayoutParams();
        params.rightMargin += (int) (fab.getWidth() * width);
        params.bottomMargin += (int) (fab.getHeight() * height);

        fab.setLayoutParams(params);
        fab.startAnimation(showAnimation);

        fab.setClickable(true);
    }

    public void hide() {

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fab.getLayoutParams();
        params.rightMargin -= (int) (fab.getWidth() * width);
        params.bottomMargin -= (int) (fab.getHeight() * height);

        fab.setLayoutParams(params);
        fab.startAnimation(hideAnimation);

        fab.setClickable(false);
    }
}
