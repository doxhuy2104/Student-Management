package com.example.studentmanagement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val studentList = mutableListOf<Student>()
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var database: StudentDatabase
    private lateinit var repository: StudentRepository

    private val addLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val student = it.data?.getSerializableExtra("student") as? Student
            if (student != null) {
                lifecycleScope.launch {
                    try {
                        val id = repository.insertStudent(student)
                        if (id != -1L) {
                            val newStudent = student.copy(id = id.toInt())
                            studentList.add(0, newStudent)
                            studentAdapter.notifyItemInserted(0)
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error adding student: ${e.message}")
                        Toast.makeText(this@MainActivity, "Lỗi khi thêm sinh viên", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private val updateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val student = it.data?.getSerializableExtra("student") as? Student
            if (student != null) {
                lifecycleScope.launch {
                    try {
                        val updatedCount = repository.updateStudent(student)
                        if (updatedCount > 0) {
                            val index = studentList.indexOfFirst { it.id == student.id }
                            if (index != -1) {
                                studentList[index] = student
                                studentAdapter.notifyItemChanged(index)
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Lỗi khi cập nhật sinh viên", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = StudentDatabase.getInstance(this)
        repository = StudentRepository(database.studentDao())

        setupRecyclerView()
        loadStudentsFromDatabase()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        studentAdapter = StudentAdapter(studentList, this,
            onDelete = { position ->
                val student = studentList[position]
                lifecycleScope.launch {
                    try {
                        repository.deleteStudentById(student.id)
                        studentList.removeAt(position)
                        studentAdapter.notifyItemRemoved(position)
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Lỗi khi xóa sinh viên", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onUpdate = { position ->
                val intent = Intent(this, UpdateStudent::class.java)
                intent.putExtra("student", studentList[position])
                updateLauncher.launch(intent)
            }
        )
        recyclerView.adapter = studentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadStudentsFromDatabase() {
        lifecycleScope.launch {
            try {
                val students = repository.getAllStudentsList()
                studentList.clear()
                studentList.addAll(students)
                studentAdapter.notifyItemRangeInserted(0, students.size)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Không thể tải danh sách sinh viên", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_student -> {
                val intent = Intent(this, AddStudent::class.java)
                addLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}