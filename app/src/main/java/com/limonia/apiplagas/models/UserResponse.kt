package com.limonia.apiplagas.models

data class UserResponse(
                        var status: String,
                        var roles: List<String>,
                        var id: String,
                        var username: String,
                        var email: String,
                        var password: String,
                        var code: String)