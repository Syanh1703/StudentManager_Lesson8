package com.example.studentmanager

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object
    {
        private const val DATABASE_NAME = "StudentDatabase"
        private const val DATABASE_VERSION = 1

        private const val SHEET_NAME = "students"

        private const val KEY_ID: String = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_LOCATION = "address"
        private const val KEY_PHONE = "phone"

        val SQL_CREATE_TABLE =("CREATE TABLE " + SHEET_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_LOCATION + " TEXT," + KEY_PHONE + " TEXT" + ")")
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + SHEET_NAME
        db!!.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    /**
     * Get the inserted folder from the database in form of the Array List
     */
    fun viewFolder(): ArrayList<DataModels> {
        val folderList: ArrayList<DataModels> = ArrayList()

        //Query to select all the items from the table
        val selectQuery = "SELECT * FROM $SHEET_NAME"
        val db = this.readableDatabase//Get permission to read the database

        /**
         * Use the cursor to read the table one by one
         */
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var name: String
        var location:String
        var id: Int
        var phone: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                location = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION))
                phone = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE))

                val folder = DataModels(id = id, name = name, address = location, phone = phone)
                folderList.add(folder)
                cursor.moveToNext()
            }
        }
        return folderList
    }

    /**
     * Insert to Database
     */
    fun addData(dataModels: DataModels):Long
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, dataModels.name)
        contentValues.put(KEY_LOCATION, dataModels.address)
        contentValues.put(KEY_PHONE, dataModels.phone)

        //Insert to the database
        val addSuccess = db.insert(SHEET_NAME, null, contentValues)
        db.close()
        return addSuccess
    }

    /**
     * Update the Folder
     */
    fun updateData(dataModels: DataModels): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, dataModels.name)
        contentValues.put(KEY_LOCATION, dataModels.address)
        contentValues.put(KEY_PHONE, dataModels.phone)

        val success = db.update(SHEET_NAME, contentValues, KEY_ID + "=" + dataModels.id, null)

        db.close()
        return success
    }

    /**
     * Delete the Folder
     */
    fun deleteFolder(data: DataModels): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, data.id)
        val deleteSuccess = db.delete(SHEET_NAME, KEY_ID + "=" + data.id, null)
        db.close()
        return deleteSuccess
    }

}