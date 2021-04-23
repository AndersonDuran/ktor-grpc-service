package com.duranzote

import io.ktor.application.*
import io.ktor.server.engine.*

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
}

fun main(args: Array<String>) {
    val env = commandLineEnvironment(args)
    val port = env.config.property("ktor.deployment.port").getString().toInt()

    embeddedServer(GRpcEngine, configure = {
        serverConfigurer = {
            serverPort = port
            addService(UserServiceImpl())
        }

    }) {}.start()
}