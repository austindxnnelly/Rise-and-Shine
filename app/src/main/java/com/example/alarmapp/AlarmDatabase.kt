package com.example.alarmapp
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.RowId

class AlarmDatabase(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    
    // alarm managers
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(p0: SQLiteDatabase?) {
        val query = "CREATE TABLE alarm_library (_id INTEGER PRIMARY KEY AUTOINCREMENT , alarm_name TEXT, alarm_hour INTEGER, alarm_minute INTEGER);"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS alarm_library")
        onCreate(p0)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addAlarm(name: String?, hour: Int, minute: Int){
        val snooze = false
        val snoozeTime = 0.toLong() //in minutes
        val cancel = false
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("alarm_name", name)
        contentValues.put("alarm_hour", hour)
        contentValues.put("alarm_minute", minute)
        db.insert("alarm_library", null, contentValues)

        // Set the alarm to start at given minute and time.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        // if snoozing is allowed -- not implemented yet
        if (snooze){
            alarmMgr?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                1000 * 60 * snoozeTime,
                alarmIntent
            )
        }
        // to cancel the alarm -- not implemented yet
        if(cancel){
            alarmMgr?.cancel(alarmIntent)
        }

    }

    fun readAllData(): Cursor? {
        val query = "SELECT * FROM alarm_library"
        val db = this.readableDatabase

        var cursor: Cursor? = null
        if(db != null){
            cursor = db.rawQuery(query, null)
        }

        return cursor
    }

    fun deleteOneRow(rowId: String){
        val db = this.writableDatabase
        db.delete("alarm_library", "_id=?", arrayOf(rowId))
    }

}