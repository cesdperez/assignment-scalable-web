# WAES Assignment: "Scalable Web"

This is a trial project for [WAES](https://www.wearewaes.com/). You can find the problem description [here](assignment-description.pdf).

## Run the application

> The only requirement is Java 8.

Run tests and build the JAR
```bash
assignment-scalable-web $ ./gradlew build
```

Run the JAR
```bash
assignment-scalable-web $ java -jar build/libs/assignment-scalable-web-1.0.0.jar
```

Or just run the application using Spring Boot Gradle plugin
```bash
assignment-scalable-web $ ./gradlew bootRun
```

> If you're a Windows user you'll want to use `gradlew.bat` instead

Both approaches will start the API listening on port 8080. This is a Spring Boot application, so you can customize it using default properties.
eg for listening on a different port:
```bash
assignment-scalable-web $ java -jar build/libs/assignment-scalable-web-1.0.0.jar --server.port=<PORT>
```

## Usage

Please check [swagger.yml](swagger.yml) for a specification of the API. If you're not familiar with Swagger, you can use https://editor.swagger.io for a better visualization of the spec or just import it to Postman. (Serving a static Swagger UI was my real goal here, but I didn't have much time left).

Of course that's not mandatory, here is a short example on how to perform a diff with cURL:

1. Save/update the _left_ part:
```bash
curl -X PUT \
  http://localhost:8080/v1/diff/1/left \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{"content":"AQ=="}'
```

2. Save/update the _right_ part:
```bash
curl -X PUT \
  http://localhost:8080/v1/diff/1/right \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{"content":"BQ=="}'
```

3. Get the "diff"
```bash
curl -X GET \
  http://localhost:8080/v1/diff/1 \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
```

Which should respond
```json
{
    "id": "1",
    "left": "AQ==",
    "right": "BQ==",
    "diff": "Left and Right have the same length, but 1 byte(s) are different"
}
```

> Remember that you need to send base64 encoded content, otherwise you'll get an error:
> ```json
> {
>     "timestamp": "2019-02-07T20:06:35.322287Z",
>     "status": "BAD_REQUEST",
>     "message": "Request body is invalid",
>     "debugMessage": "JSON parse error: Cannot deserialize value of type `byte[]` from String \"non encoded content\": Failed to decode VALUE_STRING as base64 (MIME-NO-LINEFEEDS): Illegal white space character (code 0x20) as character #4 of 4-char base64 unit: can only used between units; nested exception is com.fasterxml.jackson.databind.exc.InvalidFormatException: Cannot deserialize value of type `byte[]` from String \"encoded content\": Failed to decode VALUE_STRING as base64 (MIME-NO-LINEFEEDS): Illegal white space character (code 0x20) as character #4 of 4-char base64 unit: can only used between units\n at [Source: (PushbackInputStream); line: 1, column: 12] (through reference chain: io.cesdperez.assignmentscalableweb.dto.BinaryContent[\"content\"])"
> }
> ```

> Also remember that you need both left and right parts before computing a diff, otherwise you'll get an error:
> ```json
> {
>     "timestamp": "2019-02-07T20:08:28.732988Z",
>     "status": "PRECONDITION_FAILED",
>     "message": "The left and right parts are missing for computing diff {2}"
> }
> ```

## Setup the project in your IDE

> You don't really need to setup the project if you only want to run the application.

The process is straightforward, the only caveat is that you'll need to install the proper Lombok support for your IDE (usually it's a plugin) since I used [project Lombok](https://projectlombok.org/). Refer to the _install_ section [here](https://projectlombok.org/) if you are not familiar with it.

## Why did you use ... ?

- Spring Boot: quick way to bootstrap any kind of Java app.
- Spring MVC: battle-tested framework for building APIs.
- A database (H2): I could've just keep the diff data in some in-memory Collection (in fact I did that at first). But then I decided to persist the state in disk because:
    - it was pretty quick to do so with Spring Data and JPA.
    - ACID.
    - the API became stateless (state is now in the database) and so we're now able to scale horizontally if needed.
- Lombok: ha!, this may be the most controversial decision. It has many pros and many cons, for this simple assignment I decided to use it. I've also used it in production, but I'm OK not using it too.
- Swagger: great for documenting APIs. It comes with many advantages "for free", there are many tools around OpenAPI (eg code autogeneration, Swagger UI).

Of course all tools have their downsides too. At the end of the day I just used things I'm experienced with (because you'll ask questions) and that were good for the job.
