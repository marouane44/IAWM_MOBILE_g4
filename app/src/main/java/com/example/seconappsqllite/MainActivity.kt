package com.example.seconappsqllite

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleAdapter
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
    private lateinit var listView: ListView
    private lateinit var adapter: SimpleAdapter
    private val userList = mutableListOf<Map<String, String>>()

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
        listView = findViewById(R.id.listviewperso)

        adapter = SimpleAdapter(
            this,
            userList,
            R.layout.affichageitem,
            arrayOf("title", "description", "image"), // Now includes "image" as String
            intArrayOf(R.id.titre, R.id.description, R.id.img)
        )

        listView.adapter = adapter



        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val markStr = etMark.text.toString()

            if (name.isEmpty() || markStr.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mark = markStr.toInt()
            val isInserted = dbHelper.insertData(name, mark)
            if (isInserted) {
                Toast.makeText(this, "Student Added", Toast.LENGTH_SHORT).show()
                addUserToList(name, "Mark: $mark")
            } else {
                Toast.makeText(this, "Error Adding Student", Toast.LENGTH_SHORT).show()
            }
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

    private fun addUserToList(name: String, description: String) {
        // Add user with image resource as a string
        val user = mapOf(
            "title" to name,
            "description" to description,
            "image" to Integer.toString(R.drawable.my_image) // Convert image resource ID to String
        )
        userList.add(user)
        adapter.notifyDataSetChanged()
    }
    }


