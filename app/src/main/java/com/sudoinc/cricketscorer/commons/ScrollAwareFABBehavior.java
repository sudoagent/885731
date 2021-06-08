package com.sudoinc.cricketscorer.commons;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior
{

    // Invoke during the start of scroll
    @Override
    public boolean onStartNestedScroll (CoordinatorLayout coordinatorLayout,
                                        FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes)
    {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    //Invoke after the initial scroll
    @Override
    public void onNestedScroll( CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                                View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)
    {
        super.onNestedScroll(coordinatorLayout,child,target,dxConsumed,dyConsumed, dxUnconsumed,dyUnconsumed);

        if ( dyConsumed > 0 )
        {
            if (child.isShown())
            {
                child.hide();
            }
        }
        else if (dyConsumed < 0 )
        {
            if (!child.isShown())
            {
                child.show();
            }
        }
    }

    // constructor for the layout to recognize the scroll
    public ScrollAwareFABBehavior (Context context, AttributeSet attrs)
    {
        super();
    }
}
