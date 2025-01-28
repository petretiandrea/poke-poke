package io.github.petretiandrea.domain.translation

import arrow.core.Either

enum class TranslationType {
    YODA,
    SHAKESPEARE
}

interface Translator {
    suspend fun translate(translationType: TranslationType, text: String): Either<Error, String>
}
