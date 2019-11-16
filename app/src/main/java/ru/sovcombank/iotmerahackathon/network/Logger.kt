package ru.sovcombank.essn.log

import android.util.Log
import java.util.*
import kotlin.concurrent.getOrSet

object Logger {

    private val timeStack = ThreadLocal<LinkedList<Long>>()

    fun start() {
            val timerStack = timeStack.getOrSet {
                LinkedList()
            }
            timerStack.push(System.currentTimeMillis())
    }

    fun end(tag: String, message: String) {
            val timerStack = timeStack.getOrSet {
                LinkedList()
            }
            val start = timerStack.pop()
            Log.d(tag,  "$message, time in millis = ${System.currentTimeMillis() - start}")
    }

    fun <T> timer(tag: String, message: String, call: () -> T): T {
        return try {
            start()
            call()
        } finally {
            end(tag, message)
        }
    }

}