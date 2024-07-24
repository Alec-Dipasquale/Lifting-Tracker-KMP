package com.squalec.liftingtracker.appdatabase


expect class Logs(){
    fun log(message: String)
    fun error(message: String)
    fun debug(message: String)
}