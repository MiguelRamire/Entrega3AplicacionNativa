package com.example.petshield.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/**
 * Repositorio central para interactuar con Firebase Firestore y Auth en PetShield.
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
                guardarUsuario(nuevoUsuario) { exito, error ->
                    onResult(exito, error)
                }
            }
            .addOnFailureListener { e ->
                onResult(false, e.localizedMessage ?: "Error al registrar usuario")
            }
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

    fun guardarUsuario(usuario: UsuarioModel, onResult: (Boolean, String?) -> Unit) {
        db.collection("usuarios")
            .document(usuario.uid)
            .set(usuario, SetOptions.merge())
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

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

    fun agregarMascota(mascota: MascotaModel, onResult: (Boolean, String?) -> Unit) {
        val docRef = db.collection("mascotas").document()
        val nuevaMascota = mascota.copy(id = docRef.id)

        docRef.set(nuevaMascota)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

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

    fun eliminarMascota(mascotaId: String, onResult: (Boolean) -> Unit) {
        db.collection("mascotas")
            .document(mascotaId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // ==========================================
    // 3. VACUNAS (Carnet de Vacunación)
    // ==========================================

    fun agregarVacuna(vacuna: VacunaModel, onResult: (Boolean, String?) -> Unit) {
        val docRef = db.collection("vacunas").document()
        val nuevaVacuna = vacuna.copy(id = docRef.id)

        docRef.set(nuevaVacuna)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    fun obtenerVacunasPorMascota(mascotaId: String, onResult: (List<VacunaModel>) -> Unit) {
        db.collection("vacunas")
            .whereEqualTo("mascotaId", mascotaId)
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.toObjects(VacunaModel::class.java)
                onResult(lista)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    // ==========================================
    // 4. CLÍNICAS Y SERVICIOS
    // ==========================================

    fun obtenerClinicas(onResult: (List<ClinicaModel>) -> Unit) {
        db.collection("clinicas")
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.toObjects(ClinicaModel::class.java)
                onResult(lista)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun obtenerServiciosPorClinica(clinicaId: String, onResult: (List<ServicioModel>) -> Unit) {
        db.collection("servicios")
            .whereEqualTo("clinicaId", clinicaId)
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.toObjects(ServicioModel::class.java)
                onResult(lista)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    // ==========================================
    // 5. CITAS
    // ==========================================

    fun crearCita(cita: CitaModel, onResult: (Boolean, String?) -> Unit) {
        val docRef = db.collection("citas").document()
        val nuevaCita = cita.copy(id = docRef.id)

        docRef.set(nuevaCita)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

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

    fun agregarFavorito(favorito: FavoritoModel, onResult: (Boolean) -> Unit) {
        val docRef = db.collection("favoritos").document()
        val nuevoFav = favorito.copy(id = docRef.id)

        docRef.set(nuevoFav)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun eliminarFavorito(favoritoId: String, onResult: (Boolean) -> Unit) {
        db.collection("favoritos")
            .document(favoritoId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun obtenerFavoritosPorUsuario(usuarioId: String, onResult: (List<FavoritoModel>) -> Unit) {
        db.collection("favoritos")
            .whereEqualTo("usuarioId", usuarioId)
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.toObjects(FavoritoModel::class.java)
                onResult(lista)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
}
