package `in`.sdqali.javalin.todo

import io.javalin.Javalin
import io.javalin.core.util.Header

fun main(args: Array<String>) {
    val app = Javalin.create()
        .enableCorsForOrigin()
        .start()
    app.before("*") { ctx ->
        (ctx.header(Header.ORIGIN) ?: ctx.header(Header.REFERER))?.let {
            ctx.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, it)
        }
    }

    app.get("/") {
        ctx -> ctx.result("Hello World")
    }
}

