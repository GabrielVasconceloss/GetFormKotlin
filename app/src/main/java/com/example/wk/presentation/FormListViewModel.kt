package com.example.wk.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.wk.FormBeatsApplication
import com.example.wk.data.Form
import com.example.wk.data.FormDao

class FormListViewModel(private val formDao: FormDao): ViewModel() {

    val formListLiveData: LiveData<List<Form>> = formDao.getAll()
    companion object{
        fun create(application: Application): FormListViewModel{
            val dataBaseInstance = (application as FormBeatsApplication).getAppDatabase()
            val dao = dataBaseInstance.formDao()
            return FormListViewModel(dao)
        }
    }

}