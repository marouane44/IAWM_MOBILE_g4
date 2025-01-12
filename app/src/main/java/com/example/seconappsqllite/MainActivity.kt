package com.example.seconappsqllite

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {


    private lateinit var dbHelper: StudentDBHelper
    private lateinit var etName: EditText
    private lateinit var etMark: EditText
    private lateinit var etId: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnDelete: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        dbHelper = StudentDBHelper(this)
        etName = findViewById(R.id.etName)
        etMark = findViewById(R.id.etMark)
        etId = findViewById(R.id.etId)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnDelete = findViewById(R.id.btnDelete)
        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val markStr = etMark.text.toString()

            if (name.isEmpty() || markStr.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val mark = markStr.toInt()
            val isInserted = dbHelper.insertData(name, mark)
            Toast.makeText(this, if (isInserted) "Student Added" else "Error Adding Student", Toast.LENGTH_SHORT).show()
        }
        btnView.setOnClickListener {
            val cursor: Cursor = dbHelper.getAllData()
            if (cursor.count == 0) {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val buffer = StringBuilder()
            while (cursor.moveToNext()) {
                buffer.append("ID: ").append(cursor.getString(0)).append("\n")
                buffer.append("Name: ").append(cursor.getString(1)).append("\n")
                buffer.append("Mark: ").append(cursor.getString(2)).append("\n\n")
            }
            Toast.makeText(this, buffer.toString(), Toast.LENGTH_LONG).show()
        }
        btnDelete.setOnClickListener {
            val id = etId.text.toString()
            if (id.isEmpty()) {
                Toast.makeText(this, "Please enter ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val deletedRows = dbHelper.deleteData(id)
            Toast.makeText(this, if (deletedRows > 0) "Student Deleted" else "Error Deleting Student", Toast.LENGTH_SHORT).show()
        }
    }

    }


