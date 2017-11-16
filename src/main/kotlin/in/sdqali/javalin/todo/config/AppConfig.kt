package `in`.sdqali.javalin.todo.config

object AppConfig {
    fun port(): Int {
        System.getProperty("server.port")?.let { return it.toInt() }
        return 7000
    }

    fun rootUrl(): String {
        System.getenv("ROOT_URL")?.let { return it }
        return "http://localhost:${port()}"
    }
}
