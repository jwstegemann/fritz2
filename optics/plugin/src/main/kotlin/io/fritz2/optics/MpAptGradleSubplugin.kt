package io.fritz2.optics

import com.google.auto.service.AutoService
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@AutoService(KotlinGradleSubplugin::class) // don't forget!
class MpAptGradleSubplugin : KotlinGradleSubplugin<AbstractCompile> {
    override fun apply(
            project: Project,
            kotlinCompile: AbstractCompile,
            javaCompile: AbstractCompile?,
            variantData: Any?,
            androidProjectHandler: Any?,
            kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?
    ): List<SubpluginOption> {
        return emptyList()

    }

    override fun isApplicable(project: Project, task: AbstractCompile) =
            project.plugins.hasPlugin(MpAptGradlePlugin::class.java)


    /**
     * Just needs to be consistent with the key for CommandLineProcessor#pluginId
     */
    override fun getCompilerPluginId(): String = "MpAptPlugin"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
            groupId = "io.fritz2.optics",
            artifactId = "optics-compiler",
            version = "0.0.1" // remember to bump this version before any release!
    )

}
