package com.kaiqkt.nerv.application.web.controllers

import com.kaiqkt.nerv.application.web.requests.UserRequest
import com.kaiqkt.nerv.application.web.requests.toDomain
import com.kaiqkt.nerv.domain.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val userService: UserService,
) {
    @PostMapping
    fun create(
        @RequestBody request: UserRequest.Create,
    ): ResponseEntity<Unit> {
        userService.create(request.toDomain())

        return ResponseEntity.noContent().build()
    }
}
