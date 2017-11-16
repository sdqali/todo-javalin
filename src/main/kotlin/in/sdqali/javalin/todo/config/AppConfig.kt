package `in`.sdqali.javalin.todo.config

object AppConfig {
    fun port(): Int {
        System.getProperty("server.port")?.let { return it.toInt() }
        return 7000
    }

    fun rootUrl(): String {
        System.getenv("HEROKU_APP_NAME")?.let { return "https://$it.herokuapp.com" }
        return "http://localhost:${port()}"
    }
}
