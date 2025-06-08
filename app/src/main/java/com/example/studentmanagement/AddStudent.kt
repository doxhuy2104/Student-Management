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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddStudent : AppCompatActivity() {
    private lateinit var repository: StudentRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_student)
        enableEdgeToEdge()
        supportActionBar?.title = "Thêm sinh viên"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database = StudentDatabase.getInstance(this)
        repository = StudentRepository(database.studentDao())

        val editName = findViewById<EditText>(R.id.name)
        val editMSSV = findViewById<EditText>(R.id.mssv)
        val editEmail = findViewById<EditText>(R.id.email)
        val editPhone = findViewById<EditText>(R.id.phone)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val name = editName.text.toString().trim()
            val mssv = editMSSV.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val phone = editPhone.text.toString().trim()

            if (name.isEmpty() || mssv.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    if (repository.isMSSVExists(mssv)) {
                        Toast.makeText(this@AddStudent, "MSSV đã tồn tại!", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val student = Student(
                        id = 0,
                        name = name,
                        mssv = mssv,
                        email = email,
                        phone = phone
                    )

                    val intent = Intent(this@AddStudent, MainActivity::class.java)
                    intent.putExtra("student", student)
                    setResult(RESULT_OK, intent)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@AddStudent, "Có lỗi xảy ra: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}