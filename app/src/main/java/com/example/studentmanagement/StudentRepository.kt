package com.example.studentmanagement


class StudentRepository(private val studentDao: StudentDao) {
    fun getAllStudents(): List<Student> {
        return studentDao.getAllStudents()
    }

    suspend fun getAllStudentsList(): List<Student> {
        return studentDao.getAllStudentsList()
    }

    suspend fun insertStudent(student: Student): Long {
        return studentDao.insertStudent(student)
    }

    suspend fun updateStudent(student: Student): Int {
        return studentDao.updateStudent(student)
    }

    suspend fun deleteStudentById(id: Int): Int {
        return studentDao.deleteStudentById(id)
    }

    suspend fun isMSSVExists(mssv: String, excludeId: Int = -1): Boolean {
        return if (excludeId == -1) {
            studentDao.checkMSSVExists(mssv) > 0
        } else {
            studentDao.checkMSSVExistsExcludeId(mssv, excludeId) > 0
        }
    }
}