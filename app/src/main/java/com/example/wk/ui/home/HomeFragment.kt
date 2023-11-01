package com.example.wk.ui.home

import android.app.Activity

import com.example.wk.Form
import com.example.wk.fomrListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.wk.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

class HomeFragment : Fragment() {

    private var FormList = arrayListOf(
            Form(0,"Americanas", "Mercado de prego"),
            Form(1,"Flash", "Celular Barato, vem comprar"),
            Form(2,"Extra", "Falido nãod a para comprar"),
            Form(3,"Acai", "Nem sei o que é isso amigo"),
            Form(4,"Naruto", "È um anime muito bom"),
    )

    private lateinit var ctnContent: LinearLayout

    private val adapter = fomrListAdapter(::onListItemClicked)

    private val rusultActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val formAction = data?.getSerializableExtra(FORM_ACTION_RESULT) as FormAction
            val form: Form = formAction.form

            if(formAction.actionType == ActionType.DELETE.name){
                val newList = arrayListOf<Form>()
                    .apply {
                        addAll(FormList)
                    }
                newList.remove(form)
                if(newList.size == 0){
                    ctnContent.visibility = View.VISIBLE
                }
                adapter.submitList(newList)
                FormList = newList
                showMessage(ctnContent, "Item deleted ${form.title}")
            }else if(formAction.actionType == ActionType.CREATE.name) {
                val newList = arrayListOf<Form>()
                    .apply {
                        addAll(FormList)
                    }
                newList.add(form)
                adapter.submitList(newList)
                FormList = newList
                showMessage(ctnContent, "Item added ${form.title}")
            }else if(formAction.actionType == ActionType.UPDATE.name) {
                var tempEmpyList = arrayListOf<Form>()

                FormList.forEach {
                    if (it.id == form.id) {
                        val newItem = Form(it.id, form.title, form.description)
                        tempEmpyList.add(newItem)
                    }else{
                        tempEmpyList.add(it)
                    }
                }
                adapter.submitList(tempEmpyList)
                FormList = tempEmpyList
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
        adapter.submitList(FormList)
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