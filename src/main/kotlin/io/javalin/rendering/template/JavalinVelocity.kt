/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.rendering.template

import io.javalin.http.Context
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.JavalinRenderer
import io.javalin.rendering.util.RenderingDependency
import io.javalin.rendering.util.Util
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import java.io.StringWriter
import java.nio.charset.StandardCharsets

object JavalinVelocity : FileRenderer {

    fun init() {
        Util.throwIfNotAvailable(RenderingDependency.VELOCITY)
        JavalinRenderer.register(JavalinVelocity, ".vm", ".vtl")
    }

    private var velocityEngine: VelocityEngine? = null
    private val defaultVelocityEngine: VelocityEngine by lazy { defaultVelocityEngine() }

    @JvmStatic
    fun configure(staticVelocityEngine: VelocityEngine) {
        velocityEngine = staticVelocityEngine
    }

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context?): String {
        val stringWriter = StringWriter()
        (velocityEngine ?: defaultVelocityEngine).getTemplate(filePath, StandardCharsets.UTF_8.name()).merge(
            VelocityContext(model.toMutableMap()), stringWriter
        )
        return stringWriter.toString()
    }

    fun defaultVelocityEngine() = VelocityEngine().apply {
        setProperty("resource.loaders", "class")
        setProperty("resource.loader.class.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
    }

}