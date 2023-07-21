package com.example.finalattestationreddit.data

class Event<out T>(private val content: T) {

    var hasBeenConsumed = false
        private set

    fun consumeContent(): T? {
        return if (hasBeenConsumed) {
            null
        } else {
            hasBeenConsumed = true
            content
        }
    }

    fun peekContent(): T = content
}