package br.com.gabrielmorais

import br.com.gabrielmorais.routes.moviesRoute
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() = EngineMain.main(arrayOf())

fun Application.main() {
    config()
    moviesRoute()
}

fun Application.config() = install(ContentNegotiation){
    gson()
}


