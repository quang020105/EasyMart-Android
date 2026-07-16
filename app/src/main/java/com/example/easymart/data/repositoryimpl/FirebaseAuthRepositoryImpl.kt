package com.example.easymart.data.repositoryimpl

import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.remote.dto.FirebaseUserDto
import com.example.easymart.domain.model.User
import com.example.easymart.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    private suspend fun FirebaseAuth.readAdminClaim(forceRefresh: Boolean = false): Boolean {
        val user = auth.currentUser ?: return false
        val tokenResult = user.getIdToken(forceRefresh).await()
        return tokenResult.claims["admin"] as? Boolean ?: false
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Result<User> {
        // runCatching để bắt lỗi thay vì try catch
        return runCatching {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            //check lỗi user trả về null , vì fb không đảm bảo luôn trả về user
            val firebaseUser =
                authResult.user ?: throw Exception("Đã xảy ra lỗi trong quá trình đăng ký")
            val userId = firebaseUser.uid

            //tạo document trong firestore cho user
            val user = User(
                id = userId,
                name = name,
                email = email,
                role = "customer",
                isAdmin = false,
                createdAt = System.currentTimeMillis()
            )
            //ghi vào firestore bằng dto
            val userDto = FirebaseUserDto().apply {
                this.uid = user.id
                this.name = user.name
                this.email = user.email
                this.phone = user.phone
                this.avatarUrl = user.avatarUrl
                this.role = user.role
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
            val userId =
                authResult.user?.uid ?: throw Exception("Đã xảy ra lỗi trong quá trình đăng nhập")

            //lấy thông tin user từ firestore
            val userDto = firestore.collection("usersEM").document(userId).get().await()
                .toObject(FirebaseUserDto::class.java)
                ?: throw Exception("Người dùng không tồn tại")

            // đọc quyền của người dùng đang đăng nhập từ firebase
            val isAdmin = auth.readAdminClaim(forceRefresh = true)
            userDto.toDomain(isAdmin)
        }
    }


    override suspend fun getCurrentUserWithRole(): User? {
        val firebaseUser = auth.currentUser ?: return null
        val userDto = firestore.collection("usersEM")
            .document(firebaseUser.uid)
            .get()
            .await()
            .toObject(FirebaseUserDto::class.java)

        val isAdmin = auth.readAdminClaim()
        return userDto?.toDomain(isAdmin)
            ?: User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
                createdAt = 0L,
                role = if (isAdmin) "admin" else "customer",
                isAdmin = isAdmin
            )
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName ?: "",
            email = firebaseUser.email ?: "",
            role = "customer",
            isAdmin = false,
            createdAt = 0L
        )
    }


    //lắng nghe thay đổi user
    override fun observeCurrentUser(): Flow<User?> = callbackFlow {
//        val listener = FirebaseAuth.AuthStateListener { auth ->
//            val firebaseUser = auth.currentUser
//            //nếu user null thì gửi null
//            if (firebaseUser == null) {
//                trySend(null)
//                return@AuthStateListener
//            }
//
//            firestore.collection("usersEM").document(firebaseUser.uid)
//                .addSnapshotListener { snapshot, _ ->
//                    val userDto = snapshot?.toObject(FirebaseUserDto::class.java)
//                    trySend(userDto?.toDomain())
//                }
//        }
//       auth.addAuthStateListener(listener)
//        awaitClose { auth.removeAuthStateListener(listener) }
//
//
//        val firebaseUser = auth.currentUser ?: return null
//        return User(
//            id = firebaseUser.uid,
//            name = firebaseUser.displayName ?: "",
//            email = firebaseUser.email ?: "",
//            createdAt = 0L,
//            role = "customer",
//            isAdmin = false
//        )


        var profileListener: ListenerRegistration? = null
        var activeUserId: String? = null
        var latestUserDto: FirebaseUserDto? = null
        var adminClaim: Boolean? = null

        fun emitCurrentUser(firebaseUser: FirebaseUser) {
            val dto = latestUserDto
            val roleIndicatesAdmin = dto?.role?.equals("admin", ignoreCase = true) == true
            val isAdmin = adminClaim ?: roleIndicatesAdmin
            val user = dto?.toDomain(isAdmin)
                ?: firebaseUser.toDomain().copy(
                    role = if (isAdmin) "admin" else "customer",
                    isAdmin = isAdmin
                )
            trySend(user)
        }

        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            profileListener?.remove()
            profileListener = null
            latestUserDto = null
            adminClaim = null

            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                activeUserId = null
                trySend(null)
                return@AuthStateListener
            }

            activeUserId = firebaseUser.uid
            emitCurrentUser(firebaseUser)

            profileListener = firestore.collection("usersEM")
                .document(firebaseUser.uid)
                .addSnapshotListener { snapshot, error ->
                    if (activeUserId != firebaseUser.uid || error != null) {
                        return@addSnapshotListener
                    }

                    latestUserDto = snapshot?.toObject(FirebaseUserDto::class.java)
                    emitCurrentUser(firebaseUser)
                }

            launch {
                val resolvedAdminClaim = runCatching {
                    firebaseAuth.readAdminClaim()
                }.getOrNull()

                if (activeUserId == firebaseUser.uid && resolvedAdminClaim != null) {
                    adminClaim = resolvedAdminClaim
                    emitCurrentUser(firebaseUser)
                }
            }
        }

        auth.addAuthStateListener(listener)

        awaitClose {
            profileListener?.remove()
            auth.removeAuthStateListener(listener)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            auth.signOut()
        }
    }

    override suspend fun sendPasswordResetEmail(
        email: String,
    ): Result<Unit> {
        return runCatching {
            auth.sendPasswordResetEmail(email).await()
        }
    }
}
