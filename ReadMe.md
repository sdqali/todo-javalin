# Todo-Backend

[![Build Status](https://travis-ci.org/sdqali/todo-javalin.svg?branch=master)](https://travis-ci.org/sdqali/todo-javalin)

Example implementation for [Todo-Backend](http://www.todobackend.com/) in Kotlin using [Javalin](https://javalin.io).

## Usage
### Building
```
./gradlew clean build
```
### Running
```
./gradlew run
```
This will start the server on `http://localhost:7000`.

```
java -Dserver.port=8888 -jar build/libs/todo-javalin-0.1.0-all.jar
```
This will start the server on `http://localhost:8888`.