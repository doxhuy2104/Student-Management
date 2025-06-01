package com.example.studentmanagement

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "student_management.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_STUDENTS = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_MSSV = "mssv"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_STUDENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_MSSV TEXT NOT NULL UNIQUE,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_PHONE TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    fun addStudent(student: StudentModel): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_MSSV, student.mssv)
            put(COLUMN_EMAIL, student.email)
            put(COLUMN_PHONE, student.phone)
        }

        val id = db.insert(TABLE_STUDENTS, null, values)
        db.close()
        return id
    }

    fun getAllStudents(): MutableList<StudentModel> {
        val studentList = mutableListOf<StudentModel>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_STUDENTS ORDER BY $COLUMN_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val student = StudentModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    mssv = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MSSV)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
                )
                studentList.add(student)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return studentList
    }

    fun updateStudent(student: StudentModel): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_MSSV, student.mssv)
            put(COLUMN_EMAIL, student.email)
            put(COLUMN_PHONE, student.phone)
        }

        val result = db.update(TABLE_STUDENTS, values, "$COLUMN_ID = ?", arrayOf(student.id.toString()))
        db.close()
        return result
    }

    fun deleteStudent(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_STUDENTS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun isMSSVExists(mssv: String, excludeId: Int = -1): Boolean {
        val db = this.readableDatabase
        val query = if (excludeId == -1) {
            "SELECT * FROM $TABLE_STUDENTS WHERE $COLUMN_MSSV = ?"
        } else {
            "SELECT * FROM $TABLE_STUDENTS WHERE $COLUMN_MSSV = ? AND $COLUMN_ID != ?"
        }

        val cursor = if (excludeId == -1) {
            db.rawQuery(query, arrayOf(mssv))
        } else {
            db.rawQuery(query, arrayOf(mssv, excludeId.toString()))
        }

        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }
}