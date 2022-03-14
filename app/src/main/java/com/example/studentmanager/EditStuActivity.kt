package com.example.studentmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_edit_stu.*
import kotlinx.android.synthetic.main.center_app_name.*

class EditStuActivity : AppCompatActivity() {
    private lateinit var dataModels :DataModels
    private lateinit var dataList :ArrayList<DataModels>
    companion object
    {
        const val EDIT_TITLE = "Chỉnh sửa sinh viên"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_stu)

        val actionBar = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setCustomView(R.layout.center_app_name)

        txtViewAppName.text = EDIT_TITLE

        /**
         * Receive From Main
         */


        //Receive from ItemAdapters
        val bundle: Bundle = intent.extras!!
        dataList = intent.getSerializableExtra(MainActivity.LIST_STU) as ArrayList<DataModels>
        dataModels = bundle.getSerializable(MainActivity.KEY_EDIT) as DataModels
        etEditName.setText(dataModels.name)
        etEditAddress.setText(dataModels.address)
        etEditPhone.setText(dataModels.phone)

        btnSaveEdit.setOnClickListener {
            editStudent(dataModels)
        }
    }

    private fun editStudent(dataModels: DataModels)
    {
        val name:String = etEditName.text.toString()
        val addr:String = etEditAddress.text.toString()
        val phone :String = etEditPhone.text.toString()

        dataModels.name = name
        dataModels.address = addr
        dataModels.phone = phone

        val intent = Intent(this, EditStuActivity::class.java)
        intent.putExtra(MainActivity.EDIT_STU_NAME, name)
        intent.putExtra(MainActivity.EDIT_STU_ADDR,addr)
        intent.putExtra(MainActivity.EDIT_STU_PHONE,phone)
        setResult(MainActivity.EDIT_CODE, intent)
        finish()

        Toast.makeText(this, "Put Success", Toast.LENGTH_SHORT).show()
    }
}
