package `in`.sdqali.javalin.todo.service

import java.util.*
import kotlin.collections.ArrayList
import `in`.sdqali.javalin.todo.config.AppConfig

class TodoService {
    private val items = arrayListOf<TodoItem>()

    fun list(): List<TodoItem> {
        return items
    }

    fun clear(): List<TodoItem> {
        items.clear()
        return items
    }

    fun add(input: TodoItem): TodoItem {
        return input.title?.let {
            val item = TodoItem(title = input.title)
            items.add(item)
            return item
        }
    }

    fun get(id: UUID): TodoItem? {
        return items.find { it.id == id}
    }
}

data class TodoItem(
    val id: UUID = UUID.randomUUID(),
    var title: String,
    var completed: Boolean = false,
    val url: String = "${AppConfig.rootUrl()}/$id",
    var order: Int = -1
)
