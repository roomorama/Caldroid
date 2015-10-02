Caldroid
========

Caldroid is a fragment that display calendar with dates in a month. Caldroid can be used as embedded fragment, or as dialog fragment. User can also swipe left/right to navigate to different months.

It's very easy to customize look and feel of Caldroid using your own theme, thanks to [@crocodile2u](https://github.com/crocodile2u) contribution. There are two default themes in Caldroid (Light and Dark). You can provide your own theme based on these default themes as well.

Caldroid is fully localized. You can customize start day of the week for different countries. By default calendar start on Sunday.

Caldroid can be used with Android 2.2 and above. It is extracted from [official Roomorama application](https://play.google.com/store/apps/details?id=com.roomorama)

<img src="https://raw.github.com/roomorama/Caldroid/master/screenshot/1.png" width="270" style="margin-right:10px;">
<img src="https://raw.github.com/roomorama/Caldroid/master/screenshot/dark.png" width="270">

If you found bugs specific to Caldroid, please open a new issue on Github. However for general Android questions (about layout, drawable, etc), you probably can find more information on StackOverflow.

Setup
=====

**For Eclipse/ADT user**: please see tag [eclipse_project](https://github.com/roomorama/Caldroid/releases/tag/eclipse_project), download the source codes, check out the CaldroidSample to see how the library works. However you are strongly recommended to use Maven or gradle, because this tag is no longer supported.

To use in your project, reference the child library project as a library. If you see JAR mismatched error, replace your android-support-v4.jar to the jar inside Caldroid. Make sure you compile the project against Android 4.2 and above to allow nested fragment. See more at http://developer.android.com/about/versions/android-4.2.html#NestedFragments

**For Android Studio user**: add `compile 'com.roomorama:caldroid:2.2.0'` to your gradle build file.

**For Maven user**:
```
<dependency>
    <groupId>com.roomorama</groupId>
    <artifactId>caldroid</artifactId>
    <version>2.2.0</version>
</dependency>
```

Features
========

##Flexible setup: can be embedded or shown as dialog
If you support Android 2.2 and above, you can embed caldroid fragment in your activity with below code:

``` java
CaldroidFragment caldroidFragment = new CaldroidFragment();
Bundle args = new Bundle();
Calendar cal = Calendar.getInstance();
args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
caldroidFragment.setArguments(args);

FragmentTransaction t = getSupportFragmentManager().beginTransaction();
t.replace(R.id.calendar1, caldroidFragment);
t.commit();
```

If your app only target minSdkVersion 16 and above, you can use Caldroid too. First, you need to change your `Activity` class to `FragmentActivity`, and add support library to your project. You don't have to change how you use `android.app.Fragment`.

```java
CaldroidFragment caldroidFragment = new CaldroidFragment();
Bundle args = new Bundle();
Calendar cal = Calendar.getInstance();
args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
caldroidFragment.setArguments(args);

android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
t.replace(R.id.cal, caldroidFragment);
t.commit();
```

You can also embed caldroid fragment as a child in your fragment.

Caldroid accepts numerous arguments during start up:

``` java
public final static String
            DIALOG_TITLE = "dialogTitle",
            MONTH = "month",
            YEAR = "year",
            SHOW_NAVIGATION_ARROWS = "showNavigationArrows",
            DISABLE_DATES = "disableDates",
            SELECTED_DATES = "selectedDates",
            MIN_DATE = "minDate",
            MAX_DATE = "maxDate",
            ENABLE_SWIPE = "enableSwipe",
            START_DAY_OF_WEEK = "startDayOfWeek",
            SIX_WEEKS_IN_CALENDAR = "sixWeeksInCalendar",
            ENABLE_CLICK_ON_DISABLED_DATES = "enableClickOnDisabledDates",
            SQUARE_TEXT_VIEW_CELL = "squareTextViewCell",
            THEME_RESOURCE = "themeResource";
```

To customize the startDayOfWeek, just use

``` java
Bundle args = new Bundle();
args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.TUESDAY); // Tuesday
caldroidFragment.setArguments(args);
```

If you want to know when user clicks on disabled dates:

```java
Bundle args = new Bundle();
args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, true);
caldroidFragment.setArguments(args);
```

By default, Caldroid use square TextView to display date. However when the screen has limited space, user can switch to normal TextView instead:

```java
Bundle args = new Bundle();
args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
caldroidFragment.setArguments(args);
```

Caldroid uses `SQUARE_TEXT_VIEW_CELL` parameter internally as well. When the phone is in portrait mode, it will default `SQUARE_TEXT_VIEW_CELL` to `true`, and on landscape, `SQUARE_TEXT_VIEW_CELL` is set to `false`. If your app provides different value, Caldroid will use your value instead of the default one.

To show the caldroid fragment as a dialog, you might want to set the dialog title. There is a convenient method for that:

``` java
CaldroidFragment dialogCaldroidFragment = CaldroidFragment.newInstance("Select a date", 3, 2013);
dialogCaldroidFragment.show(getSupportFragmentManager(),"TAG");
```

## Custom theme

You can define your own theme to change the look and feel of Caldroid without having to subclass it. You should inherit from base theme `CaldroidDefault`. Here's how to create a dark theme:

```xml
    <!-- Dark theme. -->
    <style name="CaldroidDefaultDark" parent="CaldroidDefault">
        <item name="styleCaldroidViewLayout">@style/CaldroidDefaultDarkCalendarViewLayout</item>
        <item name="styleCaldroidMonthName">@style/CaldroidDefaultDarkMonthName</item>
        <item name="styleCaldroidNormalCell">@style/CaldroidDefaultDarkNormalCell</item>
        <item name="styleCaldroidSquareCell">@style/CaldroidDefaultDarkSquareCell</item>
        <item name="styleCaldroidGridView">@style/CaldroidDefaultDarkGridView</item>
    </style>

    <style name="CaldroidDefaultDarkCalendarViewLayout">
        <item name="android:background">@android:color/black</item>
    </style>

    <style name="CaldroidDefaultDarkMonthName" parent="CaldroidDefaultMonthName">
        <item name="android:textColor">@color/caldroid_white</item>
    </style>

    <style name="CaldroidDefaultDarkGridView" parent="CaldroidDefaultGridView">
        <item name="android:background">@color/caldroid_middle_gray</item>
    </style>

    <style name="CaldroidDefaultDarkCell" parent="CaldroidDefaultCell">
        <item name="android:textColor">@color/cell_text_color_dark</item>
        <item name="android:background">@drawable/cell_bg_dark</item>
    </style>

    <style name="CaldroidDefaultDarkNormalCell" parent="CaldroidDefaultDarkCell">
        <item name="android:padding">5dp</item>
    </style>

    <style name="CaldroidDefaultDarkSquareCell" parent="CaldroidDefaultDarkCell" />
```

After creating your own theme, supply it to your Caldroid fragment:

```java
Bundle args = new Bundle();
args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
caldroidFragment.setArguments(args);
```

## Custom backgrounds and text colors for different dates

It is very easy to supply different backgrounds and text colors for different dates:

``` java
// You can use any of below methods to set background colors
public void setBackgroundResourceForDates(HashMap<Date, Integer> backgroundForDateMap);
public void setBackgroundResourceForDateTimes(HashMap<DateTime, Integer> backgroundForDateTimeMap);
public void setBackgroundResourceForDate(int backgroundRes, Date date);
public void setBackgroundResourceForDateTime(int backgroundRes, DateTime dateTime);

// Below methods is to set text color
public void setTextColorForDates(HashMap<Date, Integer> textColorForDateMap);
public void setTextColorForDateTimes(HashMap<DateTime, Integer> textColorForDateTimeMap);
public void setTextColorForDate(int textColorRes, Date date);
public void setTextColorForDateTime(int textColorRes, DateTime dateTime);
```

To use these methods, you should define your colors in ```color.xml``` and background in ```drawable``` folder:

``` java
caldroidFragment.setBackgroundResourceForDate(R.color.blue, blueDate);
caldroidFragment.setBackgroundResourceForDate(R.color.green, greenDate);
caldroidFragment.setTextColorForDate(R.color.white, blueDate);
caldroidFragment.setTextColorForDate(R.color.white, greenDate);
```

You need to call ```refreshView()``` after above methods to update calendar appearance.

You can also clear the background and text color:
```java
public void clearBackgroundResourceForDate(Date date);
public void clearBackgroundResourceForDates(List<Date> dates);
public void clearTextColorForDates(List<Date> dates);
public void clearTextColorForDate(Date date);
```

## Display user events on Caldroid

Caldroid is simply an UI library and it does *not* connect to user calendar database or fetch any user's events. If your app wants to display these events on Caldroid:

- Your app needs to fetch events (from server or from user calendar database, depend on your app)

- Design a drawable for the date with event. See more here for all types of drawable you can create: http://developer.android.com/guide/topics/resources/drawable-resource.html

- Use above `setBackgroundResourceForDate` method to set the event drawable to correct date

- Call `refreshView()` to update calendar appearance

If you need to customize more for the cell, you can [supply your own cell design](https://github.com/roomorama/Caldroid#client-can-customize-look-and-feel-of-almost-all-views).

## Set min / max date

Client can use below methods:

``` java
public void setMinDate(Date minDate);
public void setMinDateFromString(String minDateString, String dateFormat);

public void setMaxDate(Date minDate);
public void setMaxDateFromString(String maxDateString, String dateFormat);
```

To refresh the calendar, just call ```refreshView()```

## Set disabled dates

Client can either provide ArrayList<Date> or ArrayList<String> to Caldroid.
``` java
public void setDisableDates(ArrayList<Date> disableDateList);
public void setDisableDatesFromString(ArrayList<String> disableDateStrings);
public void setDisableDatesFromString(ArrayList<String> disableDateStrings, String dateFormat);
```

To clear the disabled dates:
``` java
public void clearDisableDates();
```

##Select dates within a range
To select dates within a range:

``` java
public void setSelectedDates(Date fromDate, Date toDate);
public void setSelectedDateStrings(String fromDateString, String toDateString, String dateFormat);
```

To clear the selected dates:
``` java
public void clearSelectedDates();
```


##Show / Hide the navigation arrows to move to previous or next month
To show/hide the navigation arrows:

``` java
public void setShowNavigationArrows(boolean showNavigationArrows);
```

To enable / disable swipe:

``` java
public void setEnableSwipe(boolean enableSwipe);
```

Client can programmatically move the calendar (with animation) to a specified date:

``` java
public void moveToDate(Date date);
public void moveToDateTime(DateTime dateTime);
```

##Allow user to select a date and inform listener

Caldroid inform clients via CaldroidListener.

``` java
final CaldroidListener listener = new CaldroidListener() {

	@Override
	public void onSelectDate(Date date, View view) {
		Toast.makeText(getApplicationContext(), formatter.format(date),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onChangeMonth(int month, int year) {
		String text = "month: " + month + " year: " + year;
		Toast.makeText(getApplicationContext(), text,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLongClickDate(Date date, View view) {
		Toast.makeText(getApplicationContext(),
				"Long click " + formatter.format(date),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCaldroidViewCreated() {
		Toast.makeText(getApplicationContext(),
				"Caldroid view is created",
				Toast.LENGTH_SHORT).show();
	}

};

caldroidFragment.setCaldroidListener(listener);

```

##Handle screen rotation

To handle rotation properly, Caldroid provides method to get current states of the fragment:

``` java
public Bundle getSavedStates();
public void saveStatesToKey(Bundle outState, String key);
public void restoreStatesFromKey(Bundle savedInstanceState, String key);
public void restoreDialogStatesFromKey(FragmentManager manager, Bundle savedInstanceState, String key, String dialogTag)
```

Using above method, you can save current state of Caldroid on ```onSaveInstanceState(Bundle outState)``` method.

On your activity code:

``` java
@Override
protected void onSaveInstanceState(Bundle outState) {
	// TODO Auto-generated method stub
	super.onSaveInstanceState(outState);

	if (caldroidFragment != null) {
		caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
	}

	if (dialogCaldroidFragment != null) {
		dialogCaldroidFragment.saveStatesToKey(outState,
				"DIALOG_CALDROID_SAVED_STATE");
	}
}
```

Then you can restore the state in ```onCreate(Bundle savedInstanceState)``` of your activity. The algorithm is like below:

``` java
// If Activity is created after rotation
if (savedInstanceState != null) {
  caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
}

// If activity is created from fresh
else {
  Bundle args = new Bundle();
	Calendar cal = Calendar.getInstance();
  args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
  args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
  args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
  args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
  caldroidFragment.setArguments(args);
}

```

If you use Caldroid as dialog, you can use ```restoreDialogStatesFromKey```

``` java
final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
if (savedInstanceState != null) {
  dialogCaldroidFragment.restoreDialogStatesFromKey(getSupportFragmentManager(),
      					savedInstanceState, "DIALOG_CALDROID_SAVED_STATE",
      					dialogTag);
	Bundle args = dialogCaldroidFragment.getArguments();
	args.putString("dialogTitle", "Select a date");
} else {
	// Setup arguments
	Bundle bundle = new Bundle();
	// Setup dialogTitle
	bundle.putString(CaldroidFragment.DIALOG_TITLE, "Select a date");
	dialogCaldroidFragment.setArguments(bundle);
}
```

Refer to the CaldroidSampleActivity for more detail.

##Allow customized cell for the dates gridView

Caldroid provides flexible API to supply your own cell view. What you have to do is:

1) Create your own cell view layout in your project

2) Subclass CaldroidGridAdapter and override ```getView(int position, View convertView, ViewGroup parent)```. See CaldroidSampleCustomAdapter.java for more detail. Here you can customize everything: layout, text color, background for different states (normal, disable, selected)

3) Subclass CaldroidFragment to use your custom adapter instead of the default CaldroidGridAdapter. This is simplest step:

``` java
public class CaldroidSampleCustomFragment extends CaldroidFragment {

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new CaldroidSampleCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData);
	}

}
```

4) Use your new customized fragment in your project instead of the default CaldroidFragment.

To see how it works, you can uncomment this line in CaldroidSampleActivity

``` java
// final CaldroidSampleCustomFragment caldroidFragment = new CaldroidSampleCustomFragment();
```

The ```extraData``` is a ```HashMap<String, Object>```, is designed to let client injects custom data to CaldroidGridAdapter, so that data can be used to customize the date grid view. Usage is simple:

In your client code:

``` java
// To set the extraData:
HashMap<String, Object> extraData = caldroidFragment.getExtraData();
extraData.put("YOUR_CUSTOM_DATA_KEY1", yourCustomData1);
extraData.put("YOUR_CUSTOM_DATA_KEY2", yourCustomData2);

// Refresh view
caldroidFragment.refreshView();

```

In the CaldroidSampleCustomAdapter:

``` java
@Override
public View getView(int position, View convertView, ViewGroup parent) {
  // Get your data here
  ArrayList yourCustomData1 = (ArrayList) extraData.get("YOUR_CUSTOM_DATA_KEY1");
  String yourCustomData2 = (String) extraData.get("YOUR_CUSTOM_DATA_KEY2");

  // Continue to build your customized view
}
```


Basic Structure
===============

Caldroid fragment includes 4 main parts:

1) Month title view: show the month and year (e.g MARCH, 2013)

2) Navigation arrows: to navigate to next month or previous month

3) Weekday gridview: contains only 1 row and 7 columns. To display
  "SUN, MON, TUE, WED, THU, FRI, SAT"

4) An infinite view pager that allow user to swipe left/right to change month. This library is taken from https://github.com/antonyt/InfiniteViewPager

This infinite view pager recycles 4 fragment, each fragment contains a gridview with 7 columns to display the dates in month. Whenever user swipes different screen, the date grid views are updated.


Others
======

Caldroid code is simple and clean partly because of powerful [date4j](http://www.date4j.net/) library!


License
=======
See LICENSE.md


App uses Caldroid
=================
[SilentMe](https://play.google.com/store/apps/details?id=com.mindlens.alarmmanagerdemo&hl=en)

[Work Mate](http://play.google.com/store/apps/details?id=com.michaelmcneildev.workmate)

[Attendance Register (students)](https://play.google.com/store/apps/details?id=com.rudra.attendanceRegister)

[eDiary](https://play.google.com/store/apps/details?id=it.dreamerspillow.ediary)

[Moon Calendar](https://play.google.com/store/apps/details?id=fr.calendrierlunaire.android)

[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/roomorama/caldroid/trend.png)](https://bitdeli.com/free "Bitdeli Badge")
