package com.limonia.core

object EmailFormat {
    fun emailValidation(emailText: String) : Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
    }
}