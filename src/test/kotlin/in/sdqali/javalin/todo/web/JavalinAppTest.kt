package `in`.sdqali.javalin.todo.web

import `in`.sdqali.javalin.todo.service.TodoItem
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.Unirest
import org.junit.runner.RunWith
import io.javalin.Javalin
import org.junit.*
import org.junit.Assert.*
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class JavalinAppTest {

    companion object {
        init {

        }

        private lateinit var app : Javalin
        private lateinit var objectMapper: ObjectMapper

        @BeforeClass @JvmStatic fun setup() {
            val javalinApp = JavalinApp()
            app = javalinApp.creatApp()
            objectMapper = javalinApp.objectMapper()
        }

        @AfterClass @JvmStatic fun teardown() {
            app.stop()
        }
    }

    @Before
    fun cleanup() {
        Unirest.delete("http://localhost:${app.port()}/").asJson()
    }

    @Test
    fun testGetReturnsListOfTodoItems() {
        Unirest.get("http://localhost:${app.port()}/").asJson().rawBody?.let {
            val todoItems: List<TodoItem> = objectMapper.readValue(it, object: TypeReference<List<TodoItem>>() {})
        }
    }

    @Test
    fun testPostCreatesItemWithStatusAndUrl() {
        val input = TodoItem(title = "Hello", order = 456)
        Unirest.post("http://localhost:${app.port()}/").body(bytesFor(input)).asJson().rawBody?.let {
            val output : TodoItem = objectMapper.readValue(it, object: TypeReference<TodoItem>() {})
            assertEquals(input.title, output.title)
            assertEquals(false, output.completed)
            assertEquals(456, output.order)
            assertNotNull(output.url)
        }
    }

    @Test
    fun testAccessingAnItemWithItsUrl() {
        val input = TodoItem(title = "Hello")
        Unirest.post("http://localhost:${app.port()}/").body(bytesFor(input)).asJson().rawBody?.let {
            val item : TodoItem = objectMapper.readValue(it, object: TypeReference<TodoItem>() {})
            Unirest.get(item.url).asJson().rawBody?.let {
                val output : TodoItem = objectMapper.readValue(it, object: TypeReference<TodoItem>() {})
                assertEquals(input.title, output.title)
                assertEquals(false, input.completed)
                assertEquals(item.url,  output.url)
            }
        }
    }

    @Test
    fun testDeleteResultsInEmptyList() {
        Unirest.delete("http://localhost:${app.port()}/").asJson().rawBody?.let {
            val todoItems: List<TodoItem> = objectMapper.readValue(it, object: TypeReference<List<TodoItem>>() {})
            assertEquals(0, todoItems.size)
        }
    }

    @Test
    fun testListingOfItems() {
        val input = TodoItem(title = "Hello")
        Unirest.post("http://localhost:${app.port()}/").body(bytesFor(input)).asJson()

        Unirest.get("http://localhost:${app.port()}/").asJson().rawBody?.let {
            val todoItems: List<TodoItem> = objectMapper.readValue(it, object: TypeReference<List<TodoItem>>() {})
            assertEquals(1, todoItems.size)
        }
    }

    @Test
    fun testChangeTitleByPatching() {
        val input = TodoItem(title = "Hello")
        Unirest.post("http://localhost:${app.port()}/").body(bytesFor(input)).asJson().rawBody?.let {
            val item: TodoItem = objectMapper.readValue(it, object : TypeReference<TodoItem>() {})
            Unirest.patch(item.url)
                .body(bytesFor(mapOf("title" to "New title"))).asJson().rawBody?.let {
                val updatedItem: TodoItem = objectMapper.readValue(it, object: TypeReference<TodoItem>() {})
                assertEquals("New title", updatedItem.title)
            }
        }
    }

    @Test
    fun testChangeCompletednessByPatching() {
        val input = TodoItem(title = "Hello")
        Unirest.post("http://localhost:${app.port()}/").body(bytesFor(input)).asJson().rawBody?.let {
            val item: TodoItem = objectMapper.readValue(it, object : TypeReference<TodoItem>() {})
            assertEquals(false, item.completed)
            Unirest.patch(item.url)
                .body(bytesFor(mapOf("completed" to true))).asJson().rawBody?.let {
                val updatedItem: TodoItem = objectMapper.readValue(it, object: TypeReference<TodoItem>() {})
                assertEquals(true, updatedItem.completed)
            }
        }
    }

    @Test
    fun testChangeOrderByPatching() {
        val input = TodoItem(title = "Hello", order = 123)
        Unirest.post("http://localhost:${app.port()}/").body(bytesFor(input)).asJson().rawBody?.let {
            val item: TodoItem = objectMapper.readValue(it, object : TypeReference<TodoItem>() {})
            assertEquals(false, item.completed)
            Unirest.patch(item.url)
                .body(bytesFor(mapOf("order" to 765))).asJson().rawBody?.let {
                val updatedItem: TodoItem = objectMapper.readValue(it, object: TypeReference<TodoItem>() {})
                assertEquals(765, updatedItem.order)
            }
        }
    }

    @Test
    fun testDeleteAnItem() {
        val input = TodoItem(title = "Hello")
        Unirest.post("http://localhost:${app.port()}/").body(bytesFor(input)).asJson().rawBody?.let {
            val item: TodoItem = objectMapper.readValue(it, object : TypeReference<TodoItem>() {})
            assertEquals(false, item.completed)
            Unirest.delete(item.url).asJson()
        }
        Unirest.get("http://localhost:${app.port()}/").asJson().rawBody?.let {
            val todoItems: List<TodoItem> = objectMapper.readValue(it, object: TypeReference<List<TodoItem>>() {})
            assertEquals(0, todoItems.size)
        }
    }

    private fun bytesFor(input: Any) = objectMapper.writeValueAsBytes(input)
}
