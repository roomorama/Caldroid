package com.antonyt.infiniteviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * A {@link ViewPager} that allows pseudo-infinite paging with a wrap-around
 * effect. Should be used with an {@link InfinitePagerAdapter}.
 * 
 */
public class InfiniteViewPager extends ViewPager {
	private boolean enabled = true;
	private int maxHeight = 0;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public InfiniteViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InfiniteViewPager(Context context) {
		super(context);
	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		super.setAdapter(adapter);
		// offset first element so that we can scroll to the left
		setCurrentItem(getOffsetAmount());
	}

	public int getOffsetAmount() {
		if (getAdapter() instanceof InfinitePagerAdapter) {
			InfinitePagerAdapter infAdapter = (InfinitePagerAdapter) getAdapter();
			// allow for 100 back cycles from the beginning
			// should be enough to create an illusion of infinity
			// warning: scrolling to very high values (1,000,000+) results in
			// strange drawing behaviour
			return infAdapter.getRealCount() * 100;
		} else {
			return 0;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (enabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (enabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

	/**
	 * ViewPager does not respect "wrap_content". The code below tries to
	 * measure the height of the child and set the height of viewpager based on
	 * child height
	 * 
	 * It was customized from
	 * http://stackoverflow.com/questions/9313554/measuring-a-viewpager
	 * 
	 * Thanks Delyan for his brilliant code
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// If maxHeight is already calculated, then use it
		if (maxHeight > 0) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight,
					MeasureSpec.EXACTLY);

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}

		// Calculate maxHeight
		boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;

		if (wrapHeight) {
			/**
			 * The first super.onMeasure call made the pager take up all the
			 * available height. Since we really wanted to wrap it, we need to
			 * remeasure it. Luckily, after that call the first child is now
			 * available. So, we take the height from it.
			 */

			int width = getMeasuredWidth(), height = getMeasuredHeight();

			// Use the previously measured width but simplify the calculations
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
					MeasureSpec.EXACTLY);

			/*
			 * If the pager actually has any children, take the first child's
			 * height and call that our own
			 */
			if (getChildCount() > 1) {
				View firstChild = getChildAt(0);
				View secondChild = getChildAt(1);

				/*
				 * The child was previously measured with exactly the full
				 * height. Allow it to wrap this time around.
				 */
				firstChild.measure(widthMeasureSpec, MeasureSpec
						.makeMeasureSpec(height, MeasureSpec.AT_MOST));

				height = firstChild.getMeasuredHeight();

				secondChild.measure(widthMeasureSpec, MeasureSpec
						.makeMeasureSpec(height, MeasureSpec.AT_MOST));

				int height1 = secondChild.getMeasuredHeight();

				if (height == height1) {
					maxHeight = 6 * height / 5;
				} else {
					maxHeight = Math.max(height, height1);
				}
			}

			heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight,
					MeasureSpec.EXACTLY);

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}
