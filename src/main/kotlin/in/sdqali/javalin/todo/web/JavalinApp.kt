package `in`.sdqali.javalin.todo.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.javalin.Javalin
import io.javalin.core.util.Header
import io.javalin.translator.json.JavalinJacksonPlugin

data class TodoItem(
    var title: String
)

class JavalinApp {
    val objectMapper = ObjectMapper().registerModule(KotlinModule())
    fun creatApp(): Javalin  {
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
            ctx.json(listOf<TodoItem>())
        }

        app.post("/") { ctx ->
            ctx.json(ctx.bodyAsClass(TodoItem::class.java))
        }
        return app
    }

    fun objectMapper(): ObjectMapper {
        return objectMapper
    }
}
