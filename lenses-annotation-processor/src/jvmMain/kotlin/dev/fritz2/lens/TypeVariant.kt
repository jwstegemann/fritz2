package dev.fritz2.lens

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier

internal enum class TypeVariant {
    Invalid, DataClass, SealedDataClass, SealedInterface
}

internal fun KSClassDeclaration.isTypeVariant(): TypeVariant = when (classKind) {
    ClassKind.INTERFACE ->
        if (modifiers.contains(Modifier.SEALED)) TypeVariant.SealedInterface
        else TypeVariant.Invalid

    ClassKind.CLASS ->
        if (modifiers.contains(Modifier.DATA)) TypeVariant.DataClass
        else if (modifiers.contains(Modifier.SEALED)) TypeVariant.SealedDataClass
        else TypeVariant.Invalid

    else -> TypeVariant.Invalid
}