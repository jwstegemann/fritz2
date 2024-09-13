package dev.fritz2.lens

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import dev.fritz2.core.NoLens

internal fun interface LenseablePropertiesDeterminer {
    fun determine(classDeclaration: KSClassDeclaration): List<KSPropertyDeclaration>
}

internal val determineLensablePropertiesInConstructor = LenseablePropertiesDeterminer { classDeclaration ->
    val allPublicCtorProps = classDeclaration.primaryConstructor!!.parameters.filter { it.isVal }.map { it.name }
    classDeclaration.getDeclaredProperties()
        .filter { it.isPublic() && allPublicCtorProps.contains(it.simpleName) }.toList()
}
internal val determineLensablePropertiesInWholeType = LenseablePropertiesDeterminer { classDeclaration ->
    classDeclaration.getDeclaredProperties()
        .filter { it.isPublic() }
        .filter { it.annotations.none { annotation -> annotation.shortName.asString() == NoLens::class.simpleName } }
        .toList()
}