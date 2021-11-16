package com.uoc.psico.modelo

class Psicologos(
        var correo: String,
        var mombre: String,
        var direccion: String,
        var precio: String,
        var n_telefono: Int,
        var especialidades: String,
        var horario: String,
        var consulta_online: Boolean,
        var consulta_presencial: Boolean,
        var consulta_telefonica: Boolean,
        var foto: String,
        var descripcion: String,
        var puntuacion_media: Double) {
}