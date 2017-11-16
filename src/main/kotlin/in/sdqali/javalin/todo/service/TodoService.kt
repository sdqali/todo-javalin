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

    fun patch(id: UUID, finalState: Map<String, Any>): TodoItem? {
        return get(id)?.let { item ->
            finalState["title"]?.let { item.title = it.toString() }
            finalState["completed"]?.let { item.completed = it.toString().toBoolean() }
            return item
        }
    }

    fun delete(id: UUID): UUID? {
        return get(id)?.let { item ->
            items.remove(item)
            return item.id
        }
    }
}

data class TodoItem(
    val id: UUID = UUID.randomUUID(),
    var title: String,
    var completed: Boolean = false,
    val url: String = "${AppConfig.rootUrl()}/$id",
    var order: Int = -1
)
