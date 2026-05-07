package com.example.easymart.domain.model

enum class Role {
    CUSTOMER,
    ADMIN;

    companion object {
        fun fromUser(user: User): Role {
            return if (user.isAdmin || user.role.equals("admin", ignoreCase = true)) {
                ADMIN
            } else {
                CUSTOMER
            }
        }
    }
}

