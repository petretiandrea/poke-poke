package io.github.petretiandrea.infrastructure.apis.funtranslation

data class TranslationResponse(val contents: Contents)

data class Contents(val translated: String, val text: String, val translation: String)
