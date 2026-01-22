package com.example.easymart.data.repositoryimpl

import com.example.easymart.data.remote.dto.FirebaseUserDto
import com.example.easymart.domain.model.User
import com.example.easymart.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): AuthRepository {
    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Result<User> {
        // runCatching để bắt lỗi thay vì try catch
        return runCatching {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            //check lỗi user trả về null , vì fb không đảm bảo luôn trả về user
            val firebaseUser = authResult.user ?: throw Exception("Đã xảy ra lỗi trong quá trình đăng ký")
            val userId = firebaseUser.uid

            //tạo document trong firestore cho user
            val user = User(
                id = userId,
                name = name,
                email = email,
                createdAt = System.currentTimeMillis()
            )
            //ghi vào firestore bằng dto
            val userDto = FirebaseUserDto().apply {
                this.uid = user.id
                this.name = user.name
                this.email = user.email
                this.phone = user.phone
                this.avatarUrl = user.avatarUrl
                this.createdAt = user.createdAt
            }

            firestore.collection("usersEM").document(userId).set(userDto).await()
            user
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return runCatching {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Đã xảy ra lỗi trong quá trình đăng nhập")

            //lấy thông tin user từ firestore
            val userDto = firestore.collection("usersEM").document(userId).get().await()
                .toObject(FirebaseUserDto::class.java)
                ?: throw Exception("Người dùng không tồn tại")

            User(
                id = userDto.uid,
                name = userDto.name,
                email = userDto.email,
                phone = userDto.phone,
                avatarUrl = userDto.avatarUrl,
                createdAt = userDto.createdAt
            )
        }
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName ?: "",
            email = firebaseUser.email ?: "",
            createdAt = 0L
        )
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            auth.signOut()
        }
    }

}