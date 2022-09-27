/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.rendering.template

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.javalin.http.Context
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.JavalinRenderer
import io.javalin.rendering.util.RenderingDependency
import io.javalin.rendering.util.Util
import java.io.StringWriter

object JavalinPebble : FileRenderer {

    fun init() {
        Util.throwIfNotAvailable(RenderingDependency.PEBBLE)
        JavalinRenderer.register(JavalinPebble, ".peb", ".pebble")
    }

    private var pebbleEngine: PebbleEngine? = null
    private val defaultPebbleEngine: PebbleEngine by lazy { defaultPebbleEngine() }

    @JvmStatic
    fun configure(staticPebbleEngine: PebbleEngine) {
        pebbleEngine = staticPebbleEngine
    }

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context?): String {
        val compiledTemplate = (pebbleEngine ?: defaultPebbleEngine).getTemplate(filePath)
        val stringWriter = StringWriter()
        compiledTemplate.evaluate(stringWriter, model)
        return stringWriter.toString()
    }

    private fun defaultPebbleEngine() = PebbleEngine.Builder()
        .loader(ClasspathLoader())
        .strictVariables(false)
        .build()

}