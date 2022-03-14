package com.example.studentmanager

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_delete.*
import kotlinx.android.synthetic.main.dialog_update_layout.*
import kotlinx.android.synthetic.main.student_item.*

class MainActivity : AppCompatActivity() {
    private lateinit var itemList: ArrayList<DataModels>
    private lateinit var itemAdapters: ItemAdapters

    companion object {
        const val KEY_EDIT = "Edit Student"
        const val LIST_STU = "Student List"
        const val ADD_STU_NAME = "Name"
        const val ADD_STU_ADDR = "Addr"
        const val ADD_STU_PHONE = "Phone"
        const val ACTION_EDIT = "edit"
        const val EDIT_STU_NAME = "Name"
        const val EDIT_STU_ADDR = "Address"
        const val EDIT_STU_PHONE = "Phone"

        const val ADD_CODE = 17
        const val EDIT_CODE = 18
        const val DELETE_CONTENT = "Are you sure want to delete "
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val actionBar = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setCustomView(R.layout.center_app_name)

        itemList = ArrayList()
        rvStu.layoutManager = LinearLayoutManager(this)
        itemAdapters = ItemAdapters(this, getItemList())
        rvStu.adapter = itemAdapters

        //Add Student
        addStu.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun setUpFolderView() {
        if (getItemList().size > 0) {
            itemList = ArrayList()
            rvStu.layoutManager = LinearLayoutManager(this)
            itemAdapters = ItemAdapters(this, getItemList())
            rvStu.adapter = itemAdapters
        }
    }

    private fun getDataFromEdit(data :DataModels)
    {
        val name = intent.getStringExtra(EDIT_STU_NAME).toString()
        val addr = intent.getStringExtra(EDIT_STU_ADDR).toString()
        val phone = intent.getStringExtra(EDIT_STU_PHONE).toString()
        val databaseHandler = DatabaseHandler(this)
        if(name.isNotEmpty() && addr.isNotEmpty() && phone.isNotEmpty())
        {
            val status = databaseHandler.updateData(DataModels(data.id, name, addr, phone))
            if(status>-1)
            {
                Toast.makeText(this, "Edit Success", Toast.LENGTH_SHORT).show()
            }
        }
        setUpFolderView()
    }

    private fun getItemList(): ArrayList<DataModels> {
        //Create instance for database handler
        val databaseHandler = DatabaseHandler(this)

        //Call the viewData to read the folders
        val folderList: ArrayList<DataModels> = databaseHandler.viewFolder()
        folderList.add((DataModels(0, "Hello", "123 Street", "0938913133")))
        return folderList
    }


    var resultLauncher = //Receive From Others
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ADD_CODE) {
                // There are no request codes
                val data: Intent? = result.data
                val name: String
                val addr: String
                val phone: String
                if (data != null) {
                    name = data.getStringExtra(ADD_STU_NAME).toString()
                    addr = data.getStringExtra(ADD_STU_ADDR).toString()
                    phone = data.getStringExtra(ADD_STU_PHONE).toString()
                    val databaseHandler = DatabaseHandler(this)
                    val status = databaseHandler.addData(DataModels(0, name, addr, phone))
                    if (status > -1) {
                        setUpFolderView()
                    }
                }
            }
        }

    fun deleteDialog(data: DataModels) {
        val deleteDialog = Dialog(this, R.style.Theme_Dialog)
        deleteDialog.setCancelable(false)

        deleteDialog.setContentView(R.layout.dialog_delete)

        //Get name
        deleteDialog.deleteDialogContent.text = DELETE_CONTENT + data.name
        deleteDialog.tvConfirmDelete.setOnClickListener {
            val databaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteFolder(DataModels(data.id, "", "", ""))
            if (status > -1) {
                Toast.makeText(this, "Folder deleted", Toast.LENGTH_SHORT).show()
                setUpFolderView()
                deleteDialog.dismiss()
            }
        }
        deleteDialog.tvCancelDelete.setOnClickListener {
            deleteDialog.dismiss()
        }
        deleteDialog.show()
    }

    fun updateDialog(data: DataModels)
    {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update_layout)

        updateDialog.etUpdateName.setText(data.name)
        updateDialog.etUpdateAddr.setText(data.address)
        updateDialog.etUpdatePhone.setText(data.phone)

        updateDialog.tvUpdate.setOnClickListener {
            val name :String = updateDialog.etUpdateName.text.toString()
            val addr :String = updateDialog.etUpdateAddr.text.toString()
            val phone :String = updateDialog.etUpdatePhone.text.toString()
            val databaseHandler = DatabaseHandler(this)
            if(name.isNotEmpty() && addr.isNotEmpty() && phone.isNotEmpty() && phone.length!= 11)
            {
                val status = databaseHandler.updateData(DataModels(data.id, name,addr,phone))
                if(status>-1)
                {
                    Toast.makeText(this, "Student Updated", Toast.LENGTH_SHORT).show()
                    setUpFolderView()
                    updateDialog.dismiss()
                }
            }
        }
        updateDialog.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

  /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == EDIT_CODE && resultCode == RESULT_OK)
        {
            val name = intent.getStringExtra(EDIT_STU_NAME).toString()
            val addr = intent.getStringExtra(EDIT_STU_ADDR).toString()
            val phone = intent.getStringExtra(EDIT_STU_PHONE).toString()
            databaseHandler.updateData(dataModels = DataModels(dataReceived.id,name,addr,phone))
            setUpFolderView()
            Toast.makeText(this, "Update succeed", Toast.LENGTH_SHORT).show()
        }
    }*/
}