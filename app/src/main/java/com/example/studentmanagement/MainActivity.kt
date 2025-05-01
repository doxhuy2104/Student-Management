package com.example.studentmanagement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val studentList = mutableListOf<StudentModel>()
    private lateinit var studentAdapter: StudentAdapter

    private val addLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val student = it.data?.getSerializableExtra("student") as? StudentModel
            if (student != null) {
                studentList.add(0,student)
                studentAdapter.notifyItemInserted(0)
            }
        }
    }
    private val updateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val student = it.data?.getSerializableExtra("student") as? StudentModel
            if (student != null) {
                val index = studentList.indexOfFirst { it.mssv == student.mssv }
                studentList[index]=student
                studentAdapter.notifyDataSetChanged()
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
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        studentAdapter = StudentAdapter(studentList, this ,
            onDelete = { position ->
                studentList.removeAt(position)
                studentAdapter.notifyItemRemoved(position)
            },
            onUpdate={position ->
                val intent = Intent(this, UpdateStudent::class.java)
                intent.putExtra("student",studentList[position])
                updateLauncher.launch(intent)
            })
        recyclerView.adapter = studentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        studentList.add(StudentModel("Đỗ Xuân Huy", "20225331", "doxhuy2104@gmail.com", "0999999999"))
        studentAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
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