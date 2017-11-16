package `in`.sdqali.javalin.todo.web

import `in`.sdqali.javalin.todo.config.AppConfig
import `in`.sdqali.javalin.todo.service.TodoItem
import `in`.sdqali.javalin.todo.service.TodoService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.javalin.Javalin
import io.javalin.core.util.Header
import io.javalin.translator.json.JavalinJacksonPlugin
import java.util.*

class JavalinApp {
    val objectMapper = ObjectMapper().registerModule(KotlinModule())
    val todoService = TodoService()
    fun creatApp(): Javalin  {
        JavalinJacksonPlugin.configure(objectMapper)
        val app = Javalin.create()
            .enableCorsForOrigin()
            .port(AppConfig.port())
            .start()
        app.before("*") { ctx ->
            (ctx.header(Header.ORIGIN) ?: ctx.header(Header.REFERER))?.let {
                ctx.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, it)
            }
        }

        app.get("/") { ctx ->
            ctx.json(todoService.list())
        }

        app.get("/:id") { ctx ->
            val foundItem = todoService.get(UUID.fromString(ctx.param("id")))
            foundItem?.let {
                ctx.json(it)
            }
            if(foundItem == null) {
                ctx.status(404)
            }
        }

        app.post("/") { ctx ->
            ctx.json(todoService.add(ctx.bodyAsClass(TodoItem::class.java)))
        }

        app.delete("/") { ctx ->
            ctx.json(todoService.clear())
        }

        return app
    }

    fun objectMapper(): ObjectMapper {
        return objectMapper
    }
}
