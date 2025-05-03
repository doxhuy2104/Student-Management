package com.example.studentmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class UpdateStudent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_student)
        enableEdgeToEdge()
        supportActionBar?.title="Sửa sinh viên"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editName = findViewById<EditText>(R.id.name)
        val editMSSV = findViewById<EditText>(R.id.mssv)
        val editEmail = findViewById<EditText>(R.id.email)
        val editPhone = findViewById<EditText>(R.id.phone)
        val button = findViewById<Button>(R.id.button)

        var student = intent.getSerializableExtra("student")as?StudentModel
        if(student!=null) {
            editName.setText(student.name)
            editMSSV.setText(student.mssv)
            editEmail.setText(student.email)
            editPhone.setText(student.phone)

        button.setOnClickListener{
            val updateStudent = StudentModel(
                student.id,
                editName.text.toString(),
                editMSSV.text.toString(),
                editEmail.text.toString(),
                editPhone.text.toString()
            )
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("student", updateStudent)
            setResult(RESULT_OK, intent)
            finish()
        }}
    }
}