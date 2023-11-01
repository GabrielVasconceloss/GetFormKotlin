package com.example.wk.ui.home

import android.app.Activity

import com.example.wk.Form
import com.example.wk.fomrListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
            Form(0,"Title1", "Descripition1"),
            Form(1,"Title2", "Descripition2"),
    )

    private lateinit var ctnContent: LinearLayout

    private val adapter = fomrListAdapter(::openFormDetailView)

    private val rusultActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val formAction = data?.getSerializableExtra(FORM_ACTION_RESULT) as FormAction
            val form: Form = formAction.form

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
            Snackbar.make(it, "Here's Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }

        return root
    }

    private fun openFormDetailView(form: Form){
        val intent = FormDetailActivity.start(requireContext(), form)
        rusultActivity.launch(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

sealed class ActionType: Serializable{
    object DELETE : ActionType()
    object UPDATE : ActionType()
    object CREATE : ActionType()
}

data class FormAction(val form: Form, val actionType: ActionType): Serializable

const val FORM_ACTION_RESULT = "FORM_ACTION_RESULT"