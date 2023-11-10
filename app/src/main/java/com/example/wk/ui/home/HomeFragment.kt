package com.example.wk.ui.home

import android.app.Activity

import com.example.wk.data.Form
import com.example.wk.presentation.fomrListAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.wk.FormBeatsApplication
import com.example.wk.data.AppDataBase
import com.example.wk.databinding.FragmentHomeBinding
import com.example.wk.presentation.FormListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable
import java.lang.ref.WeakReference

class HomeFragment : Fragment() {

    private lateinit var ctnContent: LinearLayout

    private val adapter = fomrListAdapter(::onListItemClicked)
    private var weakLifecycleOwner: WeakReference<LifecycleOwner>? = null
    private val viewModel: FormListViewModel by lazy {
        FormListViewModel.create(requireActivity().application)
    }
    lateinit var dataBase: AppDataBase

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
        //listFromDataBase()
        recyclerView.adapter = adapter

        listFromDataBase()



        val fab: FloatingActionButton = binding.fabAdd
        fab.setOnClickListener {
            openFormListDetail(null)
        }

        val application = requireActivity().application as FormBeatsApplication
        dataBase = application.getAppDatabase()
        Log.d("Gabriel ", dataBase.toString())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use 'this' como o dono da observação
        weakLifecycleOwner = WeakReference(this)

        listFromDataBase()
    }

    private fun listFromDataBase(){
        val listObserver = Observer<List<Form>>{listFroms ->
            adapter.submitList(listFroms)
        }

        weakLifecycleOwner?.get()?.let { lifecycleOwner ->
            viewModel.formListLiveData.observe(lifecycleOwner, listObserver)
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            val myDataBaseList: List<Form> = dao.getAll()
//            adapter.submitList(myDataBaseList)
//            CoroutineScope(Dispatchers.Main).launch {
//                if(myDataBaseList.size == 0){
//                    ctnContent.visibility = View.VISIBLE
//                }else{
//                    ctnContent.visibility = View.GONE
//                }
//            }
//      }
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
           // listFromDataBase()
        }
    }
    private fun updateIntoDataBase(form: Form){
        CoroutineScope(Dispatchers.IO).launch {
            dao.update(form)
            //listFromDataBase()
        }
    }
    private fun insertIntoDataBase(form: Form){
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(form)
            //listFromDataBase()
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