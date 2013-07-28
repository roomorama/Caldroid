package com.antonyt.infiniteviewpager;

import java.util.ArrayList;

import org.joda.time.DateTime;

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

	// ******* Declaration *********
	public static final int OFFSET = 1000;

	/**
	 * dateInMonthsList is required to calculate the height correctly
	 */
	private ArrayList<DateTime> dateInMonthsList;

	/**
	 * Enable swipe
	 */
	private boolean enabled = true;

	/**
	 * A calendar height is not fixed, it may have 4, 5 or 6 rows. Set
	 * fitAllMonths to true so that the calendar will always have 6 rows
	 */
	private boolean fitAllMonths = true;

	/**
	 * Use internally to decide height of the calendar
	 */
	private int rowHeight = 0;

	// ******* Setter and getters *********
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isFitAllMonths() {
		return fitAllMonths;
	}

	public void setFitAllMonths(boolean fitAllMonths) {
		this.fitAllMonths = fitAllMonths;
		rowHeight = 0;
	}

	public ArrayList<DateTime> getDateInMonthsList() {
		return dateInMonthsList;
	}

	public void setDateInMonthsList(ArrayList<DateTime> dateInMonthsList) {
		this.dateInMonthsList = dateInMonthsList;
	}

	// ************** Constructors ********************
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
		setCurrentItem(OFFSET);
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

		// Calculate row height
		int rows = dateInMonthsList.size() / 7;

		boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;

		int height = getMeasuredHeight();
		if (wrapHeight && rowHeight == 0) {
			/*
			 * The first super.onMeasure call made the pager take up all the
			 * available height. Since we really wanted to wrap it, we need to
			 * remeasure it. Luckily, after that call the first child is now
			 * available. So, we take the height from it.
			 */

			int width = getMeasuredWidth();

			// Use the previously measured width but simplify the calculations
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
					MeasureSpec.EXACTLY);

			/*
			 * If the pager actually has any children, take the first child's
			 * height and call that our own
			 */
			if (getChildCount() > 0) {
				View firstChild = getChildAt(0);

				/*
				 * The child was previously measured with exactly the full
				 * height. Allow it to wrap this time around.
				 */
				firstChild.measure(widthMeasureSpec, MeasureSpec
						.makeMeasureSpec(height, MeasureSpec.AT_MOST));

				height = firstChild.getMeasuredHeight();
				rowHeight = height / rows;
			}
		}

		// Calculate height of the calendar
		int calHeight = 0;
		// If fitAllMonths, we need 6 rows
		if (fitAllMonths) {
			calHeight = rowHeight * 6;
		} else { // Otherwise we return correct number of rows
			calHeight = rowHeight * rows;
		}

		// If the calculated height is bigger than the parent height, set it to
		// parent height so the gridview can be scrolled
		if (calHeight > height) {
			calHeight = height;
		}

		heightMeasureSpec = MeasureSpec.makeMeasureSpec(calHeight,
				MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
