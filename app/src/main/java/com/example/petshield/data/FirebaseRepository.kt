package com.example.petshield.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

/**
 * Repositorio central con soporte para Kotlin Coroutines para interactuar con Firebase Firestore y Auth en PetShield.
 */
class FirebaseRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    // ==========================================
    // 0. AUTENTICACIÓN
    // ==========================================

    fun obtenerUsuarioActual(): FirebaseUser? = auth.currentUser

    fun cerrarSesion() {
        auth.signOut()
    }

    suspend fun registrarUsuario(
        email: String,
        password: String,
        nombre: String,
        telefono: String
    ): Result<Unit> = runCatching {
        val authResult = auth.createUserWithEmailAndPassword(email.trim(), password).await()
        val uid = authResult.user?.uid ?: throw IllegalStateException("UID nulo al registrar")
        val nuevoUsuario = UsuarioModel(
            uid = uid,
            nombre = nombre.trim(),
            correo = email.trim(),
            telefono = telefono.trim()
        )
        guardarUsuario(nuevoUsuario).getOrThrow()
    }

    fun registrarUsuario(
        email: String,
        password: String,
        nombre: String,
        telefono: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: ""
                val nuevoUsuario = UsuarioModel(
                    uid = uid,
                    nombre = nombre.trim(),
                    correo = email.trim(),
                    telefono = telefono.trim()
                )
                guardarUsuario(nuevoUsuario, onResult)
            }
            .addOnFailureListener { e ->
                onResult(false, e.localizedMessage ?: "Error al registrar usuario")
            }
    }

    suspend fun iniciarSesion(
        email: String,
        password: String
    ): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
        Unit
    }

    fun iniciarSesion(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.localizedMessage ?: "Correo o contraseña incorrectos")
            }
    }

    suspend fun iniciarSesionConGoogleToken(idToken: String): Result<Unit> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(credential).await()
        val user = authResult.user
        if (user != null) {
            val usuarioModel = UsuarioModel(
                uid = user.uid,
                nombre = user.displayName ?: "",
                correo = user.email ?: "",
                telefono = user.phoneNumber ?: "",
                fotoUrl = user.photoUrl?.toString() ?: ""
            )
            guardarUsuario(usuarioModel)
        }
        Unit
    }

    fun iniciarSesionConGoogleToken(
        idToken: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    val usuarioModel = UsuarioModel(
                        uid = user.uid,
                        nombre = user.displayName ?: "",
                        correo = user.email ?: "",
                        telefono = user.phoneNumber ?: "",
                        fotoUrl = user.photoUrl?.toString() ?: ""
                    )
                    guardarUsuario(usuarioModel) { _, _ -> }
                }
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.localizedMessage ?: "Error al autenticar con Google")
            }
    }

    // ==========================================
    // 1. USUARIOS
    // ==========================================

    suspend fun guardarUsuario(usuario: UsuarioModel): Result<Unit> = runCatching {
        db.collection("usuarios")
            .document(usuario.uid)
            .set(usuario, SetOptions.merge())
            .await()
        Unit
    }

    fun guardarUsuario(usuario: UsuarioModel, onResult: (Boolean, String?) -> Unit) {
        db.collection("usuarios")
            .document(usuario.uid)
            .set(usuario, SetOptions.merge())
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    suspend fun obtenerUsuario(uid: String): UsuarioModel? = runCatching {
        val doc = db.collection("usuarios").document(uid).get().await()
        doc.toObject(UsuarioModel::class.java)
    }.getOrNull()

    fun obtenerUsuario(uid: String, onResult: (UsuarioModel?) -> Unit) {
        db.collection("usuarios")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                onResult(doc.toObject(UsuarioModel::class.java))
            }
            .addOnFailureListener { onResult(null) }
    }

    // ==========================================
    // 2. MASCOTAS
    // ==========================================

    suspend fun agregarMascota(mascota: MascotaModel): Result<Unit> = runCatching {
        val docRef = db.collection("mascotas").document()
        val nuevaMascota = mascota.copy(id = docRef.id)
        docRef.set(nuevaMascota).await()
        Unit
    }

    fun agregarMascota(mascota: MascotaModel, onResult: (Boolean, String?) -> Unit) {
        val docRef = db.collection("mascotas").document()
        val nuevaMascota = mascota.copy(id = docRef.id)

        docRef.set(nuevaMascota)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    suspend fun obtenerMascotasPorUsuario(usuarioId: String): List<MascotaModel> = runCatching {
        val snapshot = db.collection("mascotas")
            .whereEqualTo("usuarioId", usuarioId)
            .get()
            .await()
        snapshot.toObjects(MascotaModel::class.java)
    }.getOrDefault(emptyList())

    fun obtenerMascotasPorUsuario(usuarioId: String, onResult: (List<MascotaModel>) -> Unit) {
        db.collection("mascotas")
            .whereEqualTo("usuarioId", usuarioId)
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.toObjects(MascotaModel::class.java)
                onResult(lista)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    suspend fun eliminarMascota(mascotaId: String): Boolean = runCatching {
        db.collection("mascotas").document(mascotaId).delete().await()
        true
    }.getOrDefault(false)

    // ==========================================
    // 3. VACUNAS
    // ==========================================

    suspend fun agregarVacuna(vacuna: VacunaModel): Result<Unit> = runCatching {
        val docRef = db.collection("vacunas").document()
        val nuevaVacuna = vacuna.copy(id = docRef.id)
        docRef.set(nuevaVacuna).await()
        Unit
    }

    suspend fun obtenerVacunasPorMascota(mascotaId: String): List<VacunaModel> = runCatching {
        val snapshot = db.collection("vacunas")
            .whereEqualTo("mascotaId", mascotaId)
            .get()
            .await()
        snapshot.toObjects(VacunaModel::class.java)
    }.getOrDefault(emptyList())

    // ==========================================
    // 4. CLÍNICAS Y SERVICIOS
    // ==========================================

    suspend fun obtenerClinicas(): List<ClinicaModel> = runCatching {
        val snapshot = db.collection("clinicas").get().await()
        snapshot.toObjects(ClinicaModel::class.java)
    }.getOrDefault(emptyList())

    fun obtenerClinicas(onResult: (List<ClinicaModel>) -> Unit) {
        db.collection("clinicas")
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.toObjects(ClinicaModel::class.java)
                onResult(lista)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    suspend fun agregarClinica(clinica: ClinicaModel): Result<Unit> = runCatching {
        val docRef = db.collection("clinicas").document()
        val nuevaClinica = clinica.copy(id = docRef.id)
        docRef.set(nuevaClinica).await()
        Unit
    }

    suspend fun obtenerVeterinarios(): List<VeterinarioModel> = runCatching {
        val snapshot = db.collection("veterinarios").get().await()
        snapshot.toObjects(VeterinarioModel::class.java)
    }.getOrDefault(emptyList())

    fun obtenerVeterinarios(onResult: (List<VeterinarioModel>) -> Unit) {
        db.collection("veterinarios")
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.toObjects(VeterinarioModel::class.java)
                onResult(lista)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    suspend fun obtenerServicios(): List<ServicioModel> = runCatching {
        val snapshot = db.collection("servicios").get().await()
        snapshot.toObjects(ServicioModel::class.java)
    }.getOrDefault(emptyList())

    suspend fun agregarServicio(servicio: ServicioModel): Result<Unit> = runCatching {
        val docRef = db.collection("servicios").document()
        val nuevoServicio = servicio.copy(id = docRef.id)
        docRef.set(nuevoServicio).await()
        Unit
    }

    // ==========================================
    // 5. CITAS
    // ==========================================

    suspend fun crearCita(cita: CitaModel): Result<Unit> = runCatching {
        val docRef = db.collection("citas").document()
        val nuevaCita = cita.copy(id = docRef.id)
        docRef.set(nuevaCita).await()
        Unit
    }

    fun crearCita(cita: CitaModel, onResult: (Boolean, String?) -> Unit) {
        val docRef = db.collection("citas").document()
        val nuevaCita = cita.copy(id = docRef.id)

        docRef.set(nuevaCita)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    suspend fun obtenerCitasPorUsuario(usuarioId: String): List<CitaModel> = runCatching {
        val snapshot = db.collection("citas")
            .whereEqualTo("usuarioId", usuarioId)
            .get()
            .await()
        snapshot.toObjects(CitaModel::class.java)
    }.getOrDefault(emptyList())

    fun obtenerCitasPorUsuario(usuarioId: String, onResult: (List<CitaModel>) -> Unit) {
        db.collection("citas")
            .whereEqualTo("usuarioId", usuarioId)
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.toObjects(CitaModel::class.java)
                onResult(lista)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    suspend fun cancelarCita(citaId: String, motivo: String): Boolean = runCatching {
        db.collection("citas")
            .document(citaId)
            .update(
                mapOf(
                    "estado" to "Cancelada",
                    "motivoCancelacion" to motivo
                )
            )
            .await()
        true
    }.getOrDefault(false)

    fun cancelarCita(citaId: String, motivo: String, onResult: (Boolean) -> Unit) {
        db.collection("citas")
            .document(citaId)
            .update(
                mapOf(
                    "estado" to "Cancelada",
                    "motivoCancelacion" to motivo
                )
            )
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // ==========================================
    // 6. FAVORITOS
    // ==========================================

    suspend fun agregarFavorito(favorito: FavoritoModel): Result<Unit> = runCatching {
        val docRef = db.collection("favoritos").document()
        val nuevoFavorito = favorito.copy(id = docRef.id)
        docRef.set(nuevoFavorito).await()
        Unit
    }

    suspend fun eliminarFavoritoClinica(usuarioId: String, clinicaId: String): Boolean = runCatching {
        val snapshot = db.collection("favoritos")
            .whereEqualTo("usuarioId", usuarioId)
            .whereEqualTo("clinicaId", clinicaId)
            .get()
            .await()
        for (doc in snapshot.documents) {
            doc.reference.delete().await()
        }
        true
    }.getOrDefault(false)

    suspend fun eliminarFavoritoVet(usuarioId: String, vetId: String): Boolean = runCatching {
        val snapshot = db.collection("favoritos")
            .whereEqualTo("usuarioId", usuarioId)
            .whereEqualTo("vetId", vetId)
            .get()
            .await()
        for (doc in snapshot.documents) {
            doc.reference.delete().await()
        }
        true
    }.getOrDefault(false)

    suspend fun obtenerFavoritosPorUsuario(usuarioId: String): List<FavoritoModel> = runCatching {
        val snapshot = db.collection("favoritos")
            .whereEqualTo("usuarioId", usuarioId)
            .get()
            .await()
        snapshot.toObjects(FavoritoModel::class.java)
    }.getOrDefault(emptyList())
}
