package com.duranzote

import io.grpc.Server
import io.grpc.ServerBuilder
import io.ktor.application.*
import io.ktor.server.engine.*
import java.util.concurrent.TimeUnit

object GRpcEngine : ApplicationEngineFactory<GRpcApplicationEngine, GRpcApplicationEngine.Configuration> {

    override fun create(environment: ApplicationEngineEnvironment, configure: GRpcApplicationEngine.Configuration.() -> Unit): GRpcApplicationEngine {
        return GRpcApplicationEngine(environment, configure)
    }
}

@OptIn(EngineAPI::class)
class GRpcApplicationEngine(environment: ApplicationEngineEnvironment, configure: Configuration.() -> Unit = {}) : BaseApplicationEngine(environment) {

    class Configuration : BaseApplicationEngine.Configuration() {
        var serverPort = 8888
        var serverConfigurer: ServerBuilder<*>.() -> Unit = {}
    }

    private val configuration = Configuration().apply(configure)
    private var server: Server? = null

    override fun start(wait: Boolean): ApplicationEngine {
        server = ServerBuilder
            .forPort(configuration.serverPort)
            .apply(configuration.serverConfigurer)
            .build()
            .start()

        println("Listening on port ${configuration.serverPort}...")
        server!!.awaitTermination()
        return this
    }

    override fun stop(gracePeriodMillis: Long, timeoutMillis: Long) {
        environment.monitor.raise(ApplicationStopPreparing, environment)

        if (server != null) {
            server!!.awaitTermination(gracePeriodMillis, TimeUnit.MILLISECONDS)
            server!!.shutdownNow()
        }
    }
}