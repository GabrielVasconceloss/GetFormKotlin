package com.example.wk

import java.io.Serializable

data class Form(
    val id: Int,
    val title: String,
    val description: String
): Serializable
{

}
