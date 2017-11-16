package `in`.sdqali.javalin.todo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.javalin.Javalin
import io.javalin.core.util.Header
import io.javalin.translator.json.JavalinJacksonPlugin
import java.util.*

data class TodoItem(
    var title: String
)

fun main(args: Array<String>) {
    val objectMapper = ObjectMapper().registerModule(KotlinModule())
    JavalinJacksonPlugin.configure(objectMapper)
    val app = Javalin.create()
        .enableCorsForOrigin()
        .start()
    app.before("*") { ctx ->
        (ctx.header(Header.ORIGIN) ?: ctx.header(Header.REFERER))?.let {
            ctx.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, it)
        }
    }

    app.get("/") { ctx ->
        ctx.result("Hello World")
    }

    app.post("/") { ctx ->
        ctx.json(ctx.bodyAsClass(TodoItem::class.java))
    }
}

