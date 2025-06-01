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

class UpdateStudent : AppCompatActivity() {
    private lateinit var database: Database
    private var currentStudent: StudentModel? = null
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

        database=Database(this)

        val editName = findViewById<EditText>(R.id.name)
        val editMSSV = findViewById<EditText>(R.id.mssv)
        val editEmail = findViewById<EditText>(R.id.email)
        val editPhone = findViewById<EditText>(R.id.phone)
        val button = findViewById<Button>(R.id.button)

        currentStudent = intent.getSerializableExtra("student")as?StudentModel
        if(currentStudent!=null) {
            editName.setText(currentStudent!!.name)
            editMSSV.setText(currentStudent!!.mssv)
            editEmail.setText(currentStudent!!.email)
            editPhone.setText(currentStudent!!.phone)

        button.setOnClickListener{
            val name = editName.text.toString().trim()
            val mssv = editMSSV.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val phone = editPhone.text.toString().trim()
            if (database.isMSSVExists(mssv, currentStudent!!.id)) {
                Toast.makeText(this, "MSSV đã tồn tại!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val updateStudent = StudentModel(
                currentStudent!!.id,
                name,
                mssv,
                email,
                phone
            )
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("student", updateStudent)
            setResult(RESULT_OK, intent)
            finish()
        }}
    }
}