package dev.fritz2.lens

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

internal sealed interface CheckResult {

    data object Success : CheckResult

    data class Warning(val warnings: List<Message>) : CheckResult {
        constructor(warning: Message) : this(listOf(warning))
    }

    data class Failure(val errors: List<Message>) : CheckResult {
        constructor(error: Message) : this(listOf(error))
    }

    companion object {
        fun collectMessages(vararg results: List<CheckResult>): List<Message> =
            results.flatMap { chunk ->
                chunk.flatMap { result ->
                    when (result) {
                        is Warning -> result.warnings
                        is Failure -> result.errors
                        else -> emptyList()
                    }
                }
            }
    }
}

internal fun interface RequirementChecker {
    fun checked(
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>
    ): CheckResult
}

internal class AggregatingRequirementCheckerDecorator(private val checkers: List<RequirementChecker>) :
    RequirementChecker {

    constructor(vararg checker: RequirementChecker) : this(listOf(*checker))

    fun with(checker: RequirementChecker): AggregatingRequirementCheckerDecorator =
        AggregatingRequirementCheckerDecorator(checkers + checker)

    override fun checked(
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>
    ): CheckResult {
        val (errors, warnings) = checkers
            .map { checker -> checker.checked(classDeclaration, lensableProps) }
            .filterNot { it is CheckResult.Success }
            .partition { it is CheckResult.Failure }
        return when {
            errors.isEmpty() && warnings.isNotEmpty() -> CheckResult.Warning(CheckResult.collectMessages(warnings))
            errors.isNotEmpty() -> CheckResult.Failure(CheckResult.collectMessages(warnings, errors))
            else -> CheckResult.Success
        }
    }
}

@JvmInline
internal value class DetermineReservedNames(val get: (List<KSPropertyDeclaration>) -> Set<String>)
internal data class LensesPropertyNamesAreAvailable(
    private val determineReservedNames: DetermineReservedNames,
    private val companionObject: KSClassDeclaration?
) : RequirementChecker {
    override fun checked(
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>
    ): CheckResult {
        val usedNames = companionObject?.let { companion ->
            (companion.getDeclaredFunctions() + companion.getDeclaredProperties()).map { it.simpleName.getShortName() }
        }?.toSet() ?: emptySet()

        val conflictingNames = usedNames intersect determineReservedNames.get(lensableProps)

        return if (conflictingNames.isEmpty()) {
            CheckResult.Success
        } else {
            CheckResult.Failure(
                Message(
                    Severity.Error,
                    buildString {
                        append(
                            "The companion object of $classDeclaration already defines the following" +
                                    " functions / properties: "
                        )
                        append(" {${conflictingNames.joinToString("; ")}} ")
                        append(
                            " -> Those names must not be defined! They are used for the automatic lenses generation. "
                                    + "Please rename those existing function(s) to bypass this problem!"
                        )
                    }
                )
            )
        }
    }

    companion object {
        fun forDefaultLensFactories(companionObject: KSClassDeclaration?): LensesPropertyNamesAreAvailable =
            LensesPropertyNamesAreAvailable(
                DetermineReservedNames { lensableProps -> lensableProps.map { it.simpleName.getShortName() }.toSet() },
                companionObject
            )

        fun forDownTypingLensesInSealedChild(
            classDeclaration: KSClassDeclaration,
            resolver: Resolver,
            companionObject: KSClassDeclaration?
        ): LensesPropertyNamesAreAvailable =
            LensesPropertyNamesAreAvailable(
                DetermineReservedNames { _ ->
                    val res = classDeclaration.superTypes
                        .map { "${classDeclaration.packageName.asString()}.$it" }
                        .mapNotNull { name -> resolver.getClassDeclarationByName(name) }
                        .filter {
                            it.isTypeVariant() in setOf(
                                TypeVariant.SealedDataClass,
                                TypeVariant.SealedInterface
                            )
                        }
                        .map { it.simpleName.getShortName().lowerCamelCased() }
                        .toSet()
                    res
                },
                companionObject
            )

        fun forUpTypingLensesInSealedBase(
            classDeclaration: KSClassDeclaration,
            companionObject: KSClassDeclaration?
        ): LensesPropertyNamesAreAvailable =
            LensesPropertyNamesAreAvailable(
                DetermineReservedNames {
                    val res = classDeclaration.getSealedSubclasses()
                        .map { it.simpleName.getShortName().lowerCamelCased() }
                        .toSet()
                    res
                },
                companionObject
            )
    }
}

internal val thereAreLenseableProperties = RequirementChecker { classDeclaration, lensableProps ->
    if (lensableProps.isEmpty()) {
        CheckResult.Warning(
            Message(
                Severity.Warning,
                buildString {
                    append("@Lenses annotated data class, sealed class or sealed interface $classDeclaration found,")
                    if (classDeclaration.isTypeVariant() == TypeVariant.DataClass)
                        append(" but it has no public properties defined in constructor ")
                    else
                        append(" but it has no public properties defined in ist body ")
                    append("-> can not create any lenses though...")
                }
            )
        )
    } else {
        CheckResult.Success
    }
}
internal val allImplementationsOfSealedTypeAreDataClasses =
    RequirementChecker { classDeclaration, _ ->
        val noneDataClassChildren =
            classDeclaration.getSealedSubclasses().filter { it.isTypeVariant() != TypeVariant.DataClass }.toList()
        if (noneDataClassChildren.isEmpty()) CheckResult.Success else CheckResult.Failure(
            Message(
                Severity.Error,
                buildString {
                    appendLine(
                        "All implementing children of '$classDeclaration' need to be data classes." +
                                " The following children are no data classes:"
                    )
                    noneDataClassChildren.forEach { appendLine("  - ${it.qualifiedName}") }
                }
            )
        )
    }
internal val thereIsAtLeastOneImplementationOfSealedType =
    RequirementChecker { classDeclaration, _ ->
        if (classDeclaration.getSealedSubclasses().toList().isNotEmpty()) CheckResult.Success
        else CheckResult.Warning(
            Message(
                Severity.Warning,
                "There are no subclasses for '$classDeclaration'. -> can not create any lenses though..."
            )
        )
    }

internal class CompanionObjectFound(private val companionObject: KSClassDeclaration?) : RequirementChecker {
    override fun checked(
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>
    ): CheckResult =
        if (companionObject == null) CheckResult.Failure(
            Message(
                Severity.Error,
                "The companion object for $classDeclaration is missing!  Please define it to bypass this error."
            )
        )
        else CheckResult.Success
}