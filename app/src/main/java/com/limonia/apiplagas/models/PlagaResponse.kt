package com.limonia.apiplagas.models

data class PlagaResponse(
    var id: String,
    var name: String,
    var imgURL: String,
    var description: String,
    var damage: String,
    var prevention: String) {

}
