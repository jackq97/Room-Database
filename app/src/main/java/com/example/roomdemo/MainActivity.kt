package com.example.roomdemo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.DialogUpdateRecordBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // onCreate

        // now we have access to the dao to pass in our function
        val employeeDao = (application as EmployeeApp).db.employeeDao()

        // button to add data
        binding?.buttonAddRecord?.setOnClickListener {

            // calling the function to add data
            addRecord(employeeDao)

        }

        // coroutine here cause we are getting data from our db and it can
        // take some time

        lifecycleScope.launch{
            employeeDao.fetchAllEmployees().collect {
                // yes we want to pass it in our display function but we need
                // an array list(so we can make changes) not a list
                val list = ArrayList(it)
                settingUpItemsInOurRecycleView(list, employeeDao)
            }

        }

    }
    // main
    var binding: ActivityMainBinding? = null


    // function to add record
    // we are passing our dao in it to perform functions
    private fun addRecord(employeeDao: EmployeeDao) {
        // setting the name and email
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmail?.text.toString()
        // flag check if they're not empty
        if (name.isNotEmpty() && email.isNotEmpty()) {

            // coroutine function cause it takes time to insert data
            lifecycleScope.launch{
                // making use of parameter dao and setting the name and email
                // equal to our edit text with the help of insert function
                employeeDao.insert(EmployeeEntity(name = name, email = email))
                Toast.makeText(applicationContext,
                    "record saved"
                    , Toast.LENGTH_SHORT).show()
                binding?.etName?.text?.clear()
                binding?.etEmail?.text?.clear()
            }
        } else {
            Toast.makeText(applicationContext,
                "Name or email can not be blank",
                Toast.LENGTH_SHORT).show()
        }

    }
    // function to put items in our recycle view
    // it has a parameter, we gonna pass our list inside it
    private fun settingUpItemsInOurRecycleView(employeeList: ArrayList<EmployeeEntity>,
                                               employeeDao: EmployeeDao
                                               ) {

        // first checking if the list is not empty
        if (employeeList.isNotEmpty()) {

            binding?.rvRecord?.visibility = View.VISIBLE
            // setting up the adapter
            val itemAdapter = ItemAdapter(employeeList,
                {updateId -> updateRecord(updateId, employeeDao)},
                {deleteId -> deleteRecord(deleteId, employeeDao)}
                )

            // setting up the layout
            binding?.rvRecord?.layoutManager = LinearLayoutManager(this)
            binding?.rvRecord?.adapter = itemAdapter

        } else {
            binding?.rvRecord?.visibility = View.GONE
        }
    }

    // function that determines what should happen when edit button is clicked
    private fun updateRecord(id: Int, employeeDao: EmployeeDao) {

        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        // inflating the dialog so we can get access to it's views
        val binding = DialogUpdateRecordBinding.inflate(layoutInflater)
        // setting the content view for our dialog
        updateDialog.setContentView(binding.root)
        updateDialog.setCancelable(false)

        // here we are going to get data by id from our db and populate
        // our name and email edit text so the user can update it
        // all the operations regarding fetching data and updating
        // it should be done in coroutine function
        lifecycleScope.launch{
            employeeDao.fetchEmployeeById(id).collect {
                if (it != null) {
                    binding.etName.setText(it.name)
                    binding.etEmail.setText(it.email)
                }
            }
        }
        // now we gonna run code when the user clicks on update button
        binding.btnUpdate.setOnClickListener {
            // local variables to use not empty function
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {

                lifecycleScope.launch {
                    // here we passed all the variables inside our update function
                    // note we need to pass our id as well, cause we are updating it
                    employeeDao.update(EmployeeEntity(id, name, email))
                    // toast
                    Toast.makeText(applicationContext,
                        "Record updated!",
                        Toast.LENGTH_SHORT).show()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext,
                    "Name and Email can not be empty",
                    Toast.LENGTH_SHORT).show()
            }
        }
        // now the cancel button on click listener
        binding.buttonCancel.setOnClickListener {
            updateDialog.cancel()
        }

        updateDialog.show()
    }

    private fun deleteRecord(id: Int, employeeDao: EmployeeDao) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete record")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setMessage("Are you sure you want to delete $employeeDao. ")
        lifecycleScope.launch {
            employeeDao.fetchEmployeeById(id).collect {

            }
        }

        builder.setPositiveButton("Yes"){DialogInterface, _ ->

            // when the user clicks yes we are going to delete the data
            lifecycleScope.launch {
                employeeDao.delete(EmployeeEntity(id))
            }
            Toast.makeText(applicationContext,
                "Record deleted successfully",
                Toast.LENGTH_SHORT).show()
            // after deleting we gonna dismiss the dialog
            DialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){DialogInterface, _ ->

            // dismissing dialog
            DialogInterface.dismiss()
        }

        // here we are building our alert dialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
