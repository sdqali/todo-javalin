package `in`.sdqali.javalin.todo.config

object AppConfig {
    fun port(): Int {
        System.getProperty("server.port")?.let { return it.toInt() }
        return 7000
    }
}
