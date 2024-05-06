package com.example.prm_zad1

interface Navigable {
    enum class Destination {
        LIST, ADD, EDIT
    }
    fun navigate(to: Destination, id: Long? = null)
}