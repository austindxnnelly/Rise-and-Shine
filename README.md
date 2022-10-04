# COSC345

## *Rise&Shine*
Software Engineering Project 2022
Liam Iggo, Shay Stevens, Dougal Colquhoun, Austin Donnelly

Note:
The database has been updated so an error may occur if previous database was not deleted.<br />
To fix follow steps below:<br />
1.  Go to device file explorer
2.  Open Data folder
3.  Open com.example.alarmapp folder
4.  Open databases folder
5.  Delete the database file

Codacy report:
https://app.codacy.com/gh/shaystevens/COSC345

Build:
Use android studio to build this app. 
https://developer.android.com/studio/run

USAGE:
Run this app on an android device running Android 11 or above, API level 30. Anything lower and the alarms will not come through at the desired time.
The initial screen shows the set alarms or is empty if no alarms have been set. To set an alarm click the '+' button and it will allow you to set an alarm.
When the alarm goes off it will notify the user and *soon* will require puzzles to dismiss the alarm. Swipe left to delete the alarm. Or swipe right
to open the alarm editor. If an alarm's switch is flipped and you open the editor it will
display a countdown when the alarm will go off.

Emulator:
This app has been tested on an emulated Pixel 5 API 31 System Image S

REFERENCES:
Our main source of information came from the android developer document guides and references. 
https://developer.android.com/docs

Extra Implementations:

https://github.com/xabaras/RecyclerViewSwipeDecorator
https://github.com/anugotta/FlipTimerView
https://github.com/learntodroid/SimpleAlarmClock

Many youtube videos have been very helpful.

https://www.youtube.com/watch?v=n9FN0odXqi0&ab_channel=Foxandroid
https://www.youtube.com/watch?v=BwMIbW52xLc&ab_channel=Dr.HusseinInTech
