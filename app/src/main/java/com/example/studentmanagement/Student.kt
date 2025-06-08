package com.example.studentmanagement

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "students",
    indices = [Index(value = ["mssv"], unique = true)]
)
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val mssv: String,
    val email: String,
    val phone: String
) : Serializable