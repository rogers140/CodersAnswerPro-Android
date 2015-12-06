package com.ruogu.codersanswerpro;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rogers on 2/26/14.
 * extended ViewPager to define its own slide action.
 */
public class WebViewPager extends ViewPager {

    public WebViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        //do not use ScrollView in the code_page.xml. It slows down the slide
        int width = getWidth();
        int slideWidthLeft = Math.round(width * 0.3f);
        int slideWidthRight = Math.round(width * 0.15f);
        if (x < slideWidthLeft || x > width - slideWidthRight) {
            return false;
        }
        return true;
    }
}