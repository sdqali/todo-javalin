package `in`.sdqali.javalin.todo.web

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
        }
    }

    @Test
    fun testGetReturnsListOfTodoItems() {
        Unirest.get("http://localhost:${app.port()}/").asJson().rawBody?.let {
            val todoItems: List<TodoItem> = objectMapper.readValue(it, object: TypeReference<List<TodoItem>>() {})
        }
    }

    @Test
    fun testPostRespondsWithTheItem() {
        val input = TodoItem(title = "Hello")
        Unirest.post("http://localhost:${app.port()}/").body(objectMapper.writeValueAsBytes(input)).asJson().rawBody?.let {
            val output : TodoItem = objectMapper.readValue(it, object: TypeReference<TodoItem>() {})
            assertEquals(output, input)
        }
    }

}
