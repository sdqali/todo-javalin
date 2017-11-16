package `in`.sdqali.javalin.todo

import `in`.sdqali.javalin.todo.web.JavalinApp
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.javalin.Javalin
import io.javalin.core.util.Header
import io.javalin.translator.json.JavalinJacksonPlugin
import java.util.*

fun main(args: Array<String>) {
    JavalinApp().creatApp()
}

