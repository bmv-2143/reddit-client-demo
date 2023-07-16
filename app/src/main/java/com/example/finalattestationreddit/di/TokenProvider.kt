package com.example.finalattestationreddit.di

fun interface TokenProvider {
    fun getToken(): String
}