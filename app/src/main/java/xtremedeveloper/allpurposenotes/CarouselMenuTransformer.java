package xtremedeveloper.allpurposenotes;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

public class CarouselMenuTransformer implements ViewPager.PageTransformer
{
    private int maxTranslateOffsetX;
    private ViewPager viewPager;
    public CarouselMenuTransformer(Context context) {this.maxTranslateOffsetX = dp2px(context,90);}
    public void transformPage(View view, float position) {
        if (viewPager == null) {viewPager = (ViewPager) view.getParent();}
        int leftInScreen = view.getLeft() - viewPager.getScrollX();
        int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
        int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
        float offsetRate = (float) offsetX * 0.85f / viewPager.getMeasuredWidth();
        float scaleFactor = 1 - Math.abs(offsetRate);
        if (scaleFactor > 0)
        {
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setAlpha(scaleFactor);
            view.setTranslationX(-maxTranslateOffsetX * offsetRate);
        }
        ViewCompat.setElevation(view, scaleFactor);
    }
    private int dp2px(Context context, float dipValue)
    {return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);}

}