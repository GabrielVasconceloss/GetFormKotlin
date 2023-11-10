package com.example.wk.ui.home

import android.app.Activity

import com.example.wk.data.Form
import com.example.wk.presentation.fomrListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.wk.data.AppDataBase
import com.example.wk.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class HomeFragment : Fragment() {

    private lateinit var ctnContent: LinearLayout

    private val adapter = fomrListAdapter(::onListItemClicked)

    private val dataBase by lazy {
            Room.databaseBuilder(
                requireContext(),
                AppDataBase::class.java, "getform-database"
            ).build()
    }
    private val dao by lazy { dataBase.formDao() }

    private val rusultActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val formAction = data?.getSerializableExtra(FORM_ACTION_RESULT) as FormAction
            val form: Form = formAction.form

            if(formAction.actionType == ActionType.DELETE.name){
                deleteIntoDataBase(form)
                showMessage(ctnContent, "Item deleted ${form.title}")
            }else if(formAction.actionType == ActionType.CREATE.name) {
                insertIntoDataBase(form)
                showMessage(ctnContent, "Item added ${form.title}")
            }else if(formAction.actionType == ActionType.UPDATE.name) {
                updateIntoDataBase(form)
                showMessage(ctnContent, "Item updated ${form.title}")
            }
        }
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ctnContent = binding.ctnContent
        val recyclerView: RecyclerView = binding.rvListForm
        listFromDataBase()
        recyclerView.adapter = adapter


        val fab: FloatingActionButton = binding.fabAdd
        fab.setOnClickListener {
            openFormListDetail(null)
        }

        return root
    }

    private fun onListItemClicked(form: Form){
        openFormListDetail(form)
    }

    private fun openFormListDetail(form: Form?){
        val intent = FormDetailActivity.start(requireContext(), form)
        rusultActivity.launch(intent)
    }

    private fun deleteIntoDataBase(form: Form){
        CoroutineScope(Dispatchers.IO).launch {
            dao.delete(form.id)
            listFromDataBase()
        }
    }
    private fun updateIntoDataBase(form: Form){
        CoroutineScope(Dispatchers.IO).launch {
            dao.update(form)
            listFromDataBase()
        }
    }
    private fun insertIntoDataBase(form: Form){
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(form)
            listFromDataBase()
        }
    }

    private fun listFromDataBase(){
        CoroutineScope(Dispatchers.IO).launch {
            val myDataBaseList: List<Form> = dao.getAll()
            adapter.submitList(myDataBaseList)
            CoroutineScope(Dispatchers.Main).launch {
                if(myDataBaseList.size == 0){
                    ctnContent.visibility = View.VISIBLE
                }else{
                    ctnContent.visibility = View.GONE
                }
            }
        }

    }
    private fun showMessage(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

enum class ActionType{
    DELETE,
    UPDATE,
    CREATE,
}

data class FormAction(val form: Form, val actionType: String): Serializable

const val FORM_ACTION_RESULT = "FORM_ACTION_RESULT"