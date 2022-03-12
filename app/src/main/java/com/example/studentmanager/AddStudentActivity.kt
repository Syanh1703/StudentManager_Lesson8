package com.example.studentmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_add_student.*
import kotlinx.android.synthetic.main.center_app_name.*

class AddStudentActivity : AppCompatActivity() {
    companion object
    {
        const val ADD_TITLE = "Thêm sinh viên"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val actionBar = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setCustomView(R.layout.center_app_name)

        txtViewAppName.text = ADD_TITLE

        btnSaveAdd.setOnClickListener {
            addStudent()
        }
    }

    fun addStudent()
    {
        val name :String = etAddName.text.toString()
        val address :String = etAddAddress.text.toString()
        val phone :String = etAddPhone.text.toString()

        if(name.isEmpty() || address.isEmpty() || phone.isEmpty())
        {
            Toast.makeText(this, "All of them must be filled", Toast.LENGTH_SHORT).show()
        }
        else if(phone.length <= 10)
        {
            Toast.makeText(this, "Wrong phone number", Toast.LENGTH_SHORT).show()
        }
        else
        {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MainActivity.ADD_STU_NAME, name)
            intent.putExtra(MainActivity.ADD_STU_ADDR, address)
            intent.putExtra(MainActivity.ADD_STU_PHONE, phone)
            setResult(MainActivity.ADD_CODE, intent)
            super.onBackPressed()
        }
    }

}