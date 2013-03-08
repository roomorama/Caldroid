Caldroid
========

Caldroid is a fragment that display calendar with dates in a month. Caldroid can be used as embedded fragment, or as dialog fragment.

<img src="https://raw.github.com/thomasdao/Caldroid/master/screenshot/1.png" width="270">
<img src="https://raw.github.com/thomasdao/Caldroid/master/screenshot/2.png" width="270">


Features
========

##Flexible setup: can be embedded or shown as dialog
To embed the caldroid fragment, use below code:

```
CaldroidFragment caldroidFragment = new CaldroidFragment();
Bundle args = new Bundle();
Calendar cal = Calendar.getInstance();
args.putInt("month", cal.get(Calendar.MONTH) + 1);
args.putInt("year", cal.get(Calendar.YEAR));
caldroidFragment.setArguments(args);

FragmentTransaction t = getSupportFragmentManager().beginTransaction();
t.add(R.id.calendar1, caldroidFragment);
t.commit();
```

Caldroid accepts numerous arguments during start up: 

```
int month
int year
String dialogTitle
boolean showNavigationArrows
ArrayList<String> disableDates
ArrayList<String> selectedDates
String minDate
String maxDate with yyyy-MM-dd format
```


To show the caldroid fragment as a dialog, you might want to set the dialog title. There is a convenient method for that:

```
CaldroidFragment dialogCaldroidFragment = CaldroidFragment.newInstance("Select a date", 3, 2013);
dialogCaldroidFragment.show(getSupportFragmentManager(),"TAG");
```

## Set min / max date

Client can use below methods: 

```
setMinDate(Date minDate)
setMinDateFromString(String minDateString, String dateFormat)

setMaxDate(Date minDate)
setMaxDateFromString(String maxDateString, String dateFormat)
```

To refresh the calendar, just call ```refreshView()```

## Set disabled dates

Client can either provide ArrayList<Date> or ArrayList<String> to Caldroid.
```
setDisableDates(ArrayList<Date> disableDateList)
setDisableDatesFromString(ArrayList<String> disableDateStrings)
setDisableDatesFromString(ArrayList<String> disableDateStrings, String dateFormat)
```

To clear the disabled dates:
```
clearDisableDates()
```

##Select dates within a range
To select dates within a range:

```
setSelectedDates(Date fromDate, Date toDate)
setSelectedDateStrings(String fromDateString, String toDateString, String dateFormat)
```

To clear the selected dates:
```
clearSelectedDates()
```


##Show / Hide the navigation arrows to move to previous or next month
To show/hide the navigation arrows:

```
setShowNavigationArrows(boolean showNavigationArrows)
```


##Allow user to select a date and inform listener

Caldroid inform clients via CaldroidListener. 

```
CaldroidListener listener = new CaldroidListener() {

	@Override
	public void onSelectDate(Date date, TextView textView) {
		Toast.makeText(getApplicationContext(), formatter.format(date),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onChangeMonth(int month, int year) {
		String text = "month: " + month + " year: " + year;
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
				.show();
	}

};
    
caldroidFragment.setCaldroidListener(listener);

```


Basic Structure
===============

Caldroid fragment includes 4 main parts:
  
1) Month title view: show the month and year (e.g MARCH, 2013)
  
2) Navigation arrows: to navigate to next month or previous month
  
3) Weekday gridview: contains only 1 row and 7 columns. To display
  "SUN, MON, TUE, WED, THU, FRI, SAT"
  
4) Dates gridview: contains dates within a month, and any dates in previous/
  next month. This dates gridview is main component of this library.
  

Others
======
  
Caldroid code is simple and clean partly because of powerful JODA DateTime library!


License
=======
See LICENSE.md

