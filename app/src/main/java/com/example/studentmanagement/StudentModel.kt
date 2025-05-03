package com.example.studentmanagement

import java.io.Serializable

data class StudentModel(val id:Int,val name: String, val mssv: String,val email:String,val phone:String):
    Serializable
