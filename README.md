Caldroid
========

Caldroid is a fragment that display calendar with dates in a month. Caldroid can be used as embedded fragment, or as dialog fragment.

<img src="https://raw.github.com/thomasdao/Caldroid/master/screenshot/1.png" width="150">
<img src="https://raw.github.com/thomasdao/Caldroid/master/screenshot/2.png" width="150">


Caldroid fragment includes 4 main parts:
  
1) Month title view: show the month and year (e.g MARCH, 2013)
  
2) Navigation arrows: to navigate to next month or previous month
  
3) Weekday gridview: contains only 1 row and 7 columns. To display
  "SUN, MON, TUE, WED, THU, FRI, SAT"
  
4) Dates gridview: contains dates within a month, and any dates in previous/
  next month. This dates gridview is main component of this library.
  
Caldroid fragment supports setting min/max date, selecting dates in a range, setting disabled dates, highlighting today. It includes convenient methods to  work with date and string, enable or disable the navigation arrows.
  
Caldroid code is simple and clean partly because of powerful JODA DateTime library!
