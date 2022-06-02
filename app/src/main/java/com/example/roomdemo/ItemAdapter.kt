package com.example.roomdemo

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.databinding.ListEmployeeRvBinding

// this class doesn't implement on click listeners
// that's why we made the lambda functions and we gonna pass
// our click listener to it

class ItemAdapter(private val items: ArrayList<EmployeeEntity>,
                  private val updateLister:(id:Int)-> Unit,
                  private val deleteLister:(id:Int)-> Unit
): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ListEmployeeRvBinding): RecyclerView.ViewHolder(binding.root) {

        val llMain = binding.llMain
        var tvName = binding.tvName
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( ListEmployeeRvBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false) )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context =  holder.itemView.context
        val item = items[position]

        holder.tvName.text = item.name
        holder.tvEmail.text = item.email

        if (position % 2 == 0) {

            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey))

        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        // here we will pass the on click listener
        holder.ivEdit.setOnClickListener {
            // invoke meaning we are calling the lambda function here
            // on click listener for the specific index in our item list
            updateLister.invoke(item.id)
        }

        holder.ivDelete.setOnClickListener {
            deleteLister.invoke(item.id)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}