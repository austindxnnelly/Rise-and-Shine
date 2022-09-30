package com.example.alarmapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
 * Class to act as a point of reference between our code and the database
 * This class will allow for new alarms to be stored in a table, existing
 * alarms to be updated and existing alarms to be removed from a table.
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly.
 */
class AlarmDatabase(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    private val databaseString: String = "alarm_library"

    /**
     * creates a table in the database for a new alarm
     * @param p0, the database for storing new set alarms
     */
    override fun onCreate(p0: SQLiteDatabase?) {
        val query =
            "CREATE TABLE $databaseString (_id INTEGER PRIMARY KEY AUTOINCREMENT " +
                    ", alarm_name TEXT, alarm_hour INTEGER, alarm_minute INTEGER, switch_state INTEGER);"
        p0?.execSQL(query)
    }

    /**
     * updates an existing table entry in the database for a pre existing alarm
     * @param p0, the database for storing new set alarms
     * @param p1, p2, the alarms to be dropped and recreated when they are updated.
     */
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $databaseString")
        onCreate(p0)
    }

    /**
     * adds an alarm to a table
     * @param name, the name of the alarm
     * @param hour, the time in hours that the alarm will be set for
     * @param minute, the time in minutes that the alarm will be set for
     * @param switch_state, the state of the switch
     */
    fun addAlarm(name: String?, hour: Int, minute: Int, switch_state: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("alarm_name", name)
        contentValues.put("alarm_hour", hour)
        contentValues.put("alarm_minute", minute)
        contentValues.put("switch_state", switch_state)
        db.insert(databaseString, null, contentValues)
    }

    /**
     * reads all of the alarms in the table
     */
    fun readAllData(): Cursor? {
        val query = "SELECT * FROM $databaseString"
        val db = this.readableDatabase

        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }

        return cursor
    }

    /**
     * deletes a row out of the table, which correlates to deleting a single alarm
     * @param rowId, the unique identifier of the row that is being removed.
     */
    fun deleteOneRow(rowId: String) {
        val db = this.writableDatabase
        db.delete(databaseString, "_id=?", arrayOf(rowId))
    }

    /**
     * Updates database with parameters.
     * @param rowId, the unique identifier of the row that is being edited.
     * @param name, the name of the alarm
     * @param hour, the time in hours that the alarm will be set for
     * @param minute, the time in minutes that the alarm will be set for
     * @param switch_state, the state of the switch
     */
    fun updateDatabase(rowId: String, name: String, hour: Int, minute: Int, switch_state: Int){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("alarm_name", name)
        contentValues.put("alarm_hour", hour)
        contentValues.put("alarm_minute", minute)
        contentValues.put("switch_state", switch_state)
        db.update(databaseString, contentValues, "_id=?", arrayOf(rowId))
    }

}