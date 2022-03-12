package com.example.studentmanager

import java.io.Serializable


data class DataModels (val id:Int, var name:String, var address:String, var phone:String) :Serializable{
}