package com.duranzote

import services.UserRequest
import services.UserResponse
import services.UserServiceGrpcKt

class UserServiceImpl : UserServiceGrpcKt.UserServiceCoroutineImplBase() {

    override suspend fun getUser(request: UserRequest): UserResponse {
        println("Recebendo ${request.age} and ${request.name}!")

        return UserResponse.newBuilder()
            .setName("Anderson Duran")
            .setCountry("Brazil")
            .setEmailAddress("andersonduran@gmail.com")
            .setActive(true)
            .build()
    }
}