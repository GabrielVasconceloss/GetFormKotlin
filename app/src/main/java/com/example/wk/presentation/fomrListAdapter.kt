package com.example.wk.presentation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wk.R
import com.example.wk.data.Form

class fomrListAdapter(
    private val openFormDetailView: (form: Form) -> Unit
) : androidx.recyclerview.widget.ListAdapter<Form, fomrListViewHolder>(fomrListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): fomrListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_form, parent, false)
        return fomrListViewHolder(view)
    }

    override fun onBindViewHolder(holder: fomrListViewHolder, position: Int) {
        var form = getItem(position)
        holder.bind(form, openFormDetailView)
    }

    companion object : DiffUtil.ItemCallback<Form>(){
        override fun areItemsTheSame(oldItem: Form, newItem: Form): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Form, newItem: Form): Boolean {
            return oldItem.title == newItem.description &&
                    oldItem.description == newItem.description
        }

    }
}

class fomrListViewHolder(private val view:View) : RecyclerView.ViewHolder(view){

    private var tvFormTitle = view.findViewById<TextView>(R.id.tv_form_title)
    private var tvFormDescription = view.findViewById<TextView>(R.id.tv_form_description)

    fun bind(form: Form, openFormDetailView:(form: Form) -> Unit){
        tvFormTitle.text = form.title
        tvFormDescription.text = form.description

        view.setOnClickListener{
            openFormDetailView.invoke(form)
        }
    }
}