package com.rental.transport.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

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

    public class FAB_Float_on_Scroll extends FloatingActionButton.Behavior {

        public FAB_Float_on_Scroll(Context context, AttributeSet attrs) {
            super();
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

            //child -> Floating Action Button
            if (dyConsumed > 0) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                int fab_bottomMargin = layoutParams.bottomMargin;
                child.animate().translationY(child.getHeight() + fab_bottomMargin).setInterpolator(new LinearInterpolator()).start();
            } else if (dyConsumed < 0) {
                child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
            }
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
            return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }
    }

    public class FAB_Hide_on_Scroll extends FloatingActionButton.Behavior {

        public FAB_Hide_on_Scroll(Context context, AttributeSet attrs) {
            super();
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

            //child -> Floating Action Button
            if (child.getVisibility() == View.VISIBLE && dyConsumed > 0) {
                child.hide();
            } else if (child.getVisibility() == View.GONE && dyConsumed < 0) {
                child.show();
            }
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
            return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }
    }
}
