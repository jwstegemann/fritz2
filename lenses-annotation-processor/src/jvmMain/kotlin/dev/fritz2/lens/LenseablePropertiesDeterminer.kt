package dev.fritz2.lens

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

internal fun interface LenseablePropertiesDeterminer {
    fun determine(classDeclaration: KSClassDeclaration): List<KSPropertyDeclaration>
}

internal val determineLensablePropertiesInConstructor = LenseablePropertiesDeterminer { classDeclaration ->
    val allPublicCtorProps = classDeclaration.primaryConstructor!!.parameters.filter { it.isVal }.map { it.name }
    classDeclaration.getDeclaredProperties()
        .filter { it.isPublic() && allPublicCtorProps.contains(it.simpleName) }.toList()
}
internal val determineLensablePropertiesInBody = LenseablePropertiesDeterminer { classDeclaration ->
    classDeclaration.getDeclaredProperties().filter { it.isPublic() }.toList()
}