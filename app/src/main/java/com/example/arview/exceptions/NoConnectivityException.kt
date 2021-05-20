package com.example.arview.exceptions
import java.io.IOException

class NoConnectivityException : IOException(){
    override val message: String?
        get() = "Нет связи с сервером. \nВозможно отключена сеть!"
}