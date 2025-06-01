package com.example.studentmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class AddStudent : AppCompatActivity() {
    private lateinit var database: Database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_student)
        enableEdgeToEdge()
        supportActionBar?.title="Thêm sinh viên"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = Database(this)

        val editName = findViewById<EditText>(R.id.name)
        val editMSSV = findViewById<EditText>(R.id.mssv)
        val editEmail = findViewById<EditText>(R.id.email)
        val editPhone = findViewById<EditText>(R.id.phone)
        val button = findViewById<Button>(R.id.button)


        button.setOnClickListener{
//            val studentSize = intent.getIntExtra("studentListSize", 0)
            val name = editName.text.toString().trim()
            val mssv = editMSSV.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val phone = editPhone.text.toString().trim()
            if (database.isMSSVExists(mssv)) {
                Toast.makeText(this, "MSSV đã tồn tại!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val student = StudentModel(
                id = 0,
                editName.text.toString(),
                editMSSV.text.toString(),
                editEmail.text.toString(),
                editPhone.text.toString()
            )
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("student", student)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}