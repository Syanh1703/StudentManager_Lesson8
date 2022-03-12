package com.example.studentmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class ItemAdapters(val context: Context, val items: ArrayList<DataModels>) :
    RecyclerView.Adapter<ItemAdapters.ViewHolder>() {

    //Interface to interact with item in the Recycler View
    interface onItemClickListener {
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fullName: TextView = view.findViewById(R.id.txtViewStuName)
        val location: TextView = view.findViewById(R.id.txtViewStuAddress)
        val phone: TextView = view.findViewById(R.id.txtViewStuPhone)
        val deleteBtn: ImageView = view.findViewById(R.id.imgBtnDeleteStu)
        val stuItem :LinearLayout = view.findViewById(R.id.studentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items[position]
        if (holder is ViewHolder) {
            holder.location.text = item.address
            holder.fullName.text = item.name
            holder.phone.text = item.phone

            holder.stuItem.setOnClickListener(object : onItemClickListener, View.OnClickListener {

                override fun onClick(v: View?) {
                    val intent = Intent(context, EditStuActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable(MainActivity.KEY_EDIT, item)
                    bundle.putSerializable(MainActivity.LIST_STU, items as Serializable)
                    intent.putExtras(bundle)
                    intent.setAction(MainActivity.ACTION_EDIT)
                    context.startActivity(intent)
                }
            })
            if(context is MainActivity)
            {
                holder.deleteBtn.setOnClickListener {
                    context.deleteDialog(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}