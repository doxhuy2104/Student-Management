package com.example.studentmanagement

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import android.view.ContextMenu
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog

class StudentAdapter(val studentList:MutableList<Student>, private val context: Context,private val onDelete:(Int)->Unit,private val onUpdate:(Int)->Unit):RecyclerView.Adapter<StudentAdapter.StudentViewHolder>(){
    class StudentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.textView)
        val mssv: TextView = itemView.findViewById(R.id.textView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.student_item,
            parent, false)
        return StudentViewHolder(layout)
    }
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.name.text = student.name
        holder.mssv.text = student.mssv

        holder.itemView.setOnClickListener{
            val currentPosition = holder.adapterPosition
            val popupMenu = PopupMenu(context, holder.itemView)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.update -> {
                        onUpdate(currentPosition)
                        true
                    }
                    R.id.delete -> {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                        builder
                            .setTitle("Xác nhận xoá")
                            .setMessage("Bạn có chắc chắn muốn xoá sinh viên ${student.name.toString()}")
                            .setPositiveButton("Có") { dialog, which ->
                                onDelete(currentPosition)
                            }
                            .setNegativeButton("Không") { dialog, which ->

                            }
                        val dialog: AlertDialog = builder.create()
                        dialog.show()

                        true
                    }
                    R.id.call -> {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${student.phone.toString()}")
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        }
                        true
                    }
                    R.id.email -> {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:${student.email.toString()}") // Only email apps handle this.
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
            true
        }
    }

    override fun getItemCount(): Int = studentList.size

}