package com.example.studentmanagement

import androidx.room.*

@Dao
interface StudentDao {
    @Query("SELECT * FROM students ORDER BY id DESC")
    fun getAllStudents(): List<Student>

    @Query("SELECT * FROM students ORDER BY id DESC")
    suspend fun getAllStudentsList(): List<Student>

    @Query("SELECT COUNT(*) FROM students WHERE mssv = :mssv")
    suspend fun checkMSSVExists(mssv: String): Int

    @Query("SELECT COUNT(*) FROM students WHERE mssv = :mssv AND id != :excludeId")
    suspend fun checkMSSVExistsExcludeId(mssv: String, excludeId: Int): Int

    @Insert
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student): Int

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteStudentById(id: Int): Int
}