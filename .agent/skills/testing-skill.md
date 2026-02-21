---
name: spring-kotlin-testing
description: >-
  Tests Spring Boot applications written in Kotlin using JUnit 5, MockK, RestAssured,
  Testcontainers and MockServer. Activates when writing or modifying unit tests,
  integration tests, debugging test failures, TDD, assertions, mocking dependencies,
  testing edge cases, validation, HTTP APIs or verifying application behavior end-to-end.
---

# Spring Kotlin Testing

## When to Apply

Activate this skill when:

- Creating or modifying **unit tests**
- Creating or modifying **integration tests**
- Debugging failing tests
- Writing tests using **Given / When / Then**
- Testing validation rules, edge cases, if/else branches
- Testing REST APIs as a real user
- Mocking external services or infrastructure
- Using Testcontainers (PostgreSQL, Redis, etc.)

---

## Testing Stack

- **Language:** Kotlin
- **Test framework:** JUnit 5 (Jupiter)
- **Mocking:** MockK
- **Assertions:** kotlin.test / JUnit
- **HTTP tests:** RestAssured
- **Spring context:** `@SpringBootTest`
- **Containers:** Testcontainers
- **External HTTP mocks:** MockServer

---

## Test Naming Convention

All tests **must** follow **Given / When / Then** semantics in the test name, using **then** to describe the outcome.

### Examples

- `given an invalid request when creating a project then return validation errors`
- `given a missing header when calling endpoint then return bad request`
- `given an existing project when creating with same name then return conflict`

---

## General Rules

- **No Comments:** Do not use comments like `// Given`, `// When`, or `// Then` inside test methods. Use white space to separate the logical sections of the test instead.
- **Readability:** Tests must be readable as user behavior.
- **Fail Fast:** Prefer failing fast.
- **Production Code:** Tests are production code; treat them with the same quality standards.
- **Autonomous Analysis:** When asked to test a component (Controller, Service, etc.), you must autonomously analyze all business rules, edge cases, and validation logic in the target code and its dependencies to ensure 100% logical coverage without needing manual instructions for each scenario.

---

## Unit Tests Guidelines

### Structure

Separate the setup (Given), execution (When), and assertions (Then) using a single empty line between them.

### Example – Unit Test with MockK

```kotlin
class ErrorHandlerTest {

    private val webRequest: WebRequest = mockk()
    private val errorHandler = ErrorHandler()

    @Test
    fun `given an InvalidRequestException when handling then return all fields errors with associated message`() {
        val exception = InvalidRequestException(mapOf("field" to "invalid"))

        val response = errorHandler.handleInvalidRequestException(exception)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("invalid", response.body?.details?.get("field"))
        assertEquals("Invalid request", response.body?.message)
    }
}
```

### Mocking Rules

- Always use MockK.
- Prefer `mockk()` over relaxed mocks.
- Explicitly define behavior with `every { } returns`.
- Do not mock what you don’t control unless necessary.

---

## Integration Tests Guidelines

### Base Integration Test Class

All integration tests must extend a common base class.

Responsibilities:

- Start Spring context.
- Configure RestAssured.
- Clean database state before each test.

```kotlin
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class IntegrationTest {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var projectRepository: ProjectRepository

    @BeforeAll
    fun setup() {
        RestAssured.config =
            RestAssured.config()
                .objectMapperConfig(
                    ObjectMapperConfig(ObjectMapperType.JACKSON_2)
                        .jackson2ObjectMapperFactory { _, _ -> mapper }
                )

        RestAssured.baseURI = "http://localhost:$port"
    }

    @BeforeEach
    fun cleanDatabase() {
        projectRepository.deleteAll()
    }
}
```

### HTTP Integration Tests (RestAssured)

```kotlin
@Test
fun `given a request to create a project when request is invalid then return validation errors`() {
    val request = ProjectRequest.Create(
        name = "",
        description = "description".repeat(255)
    )

    val response =
        given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/v1/projects")
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .extract()
            .`as`(ErrorResponse::class.java)

    assertEquals("input should not be blank", response.details["name"])
    assertEquals("must not exceed 255 characters", response.details["description"])
}
```

### Rules

- Always assert HTTP status.
- Deserialize response to real DTOs.
- Never assert raw JSON strings.

---

## External HTTP Dependencies (MockServer)

For external HTTP calls, use MockServer via a shared holder.

### MockServer Holder

```kotlin
abstract class MockServerHolder {

    companion object {
        private const val PORT: Int = 8081
        private val mockServer = ClientAndServer.startClientAndServer(PORT)
        private val baseUrl = "http://127.0.0.1:${mockServer.localPort}"
    }

    protected abstract fun domainPath(): String

    fun baseUrl() = "$baseUrl${domainPath()}"

    fun reset() =
        mockServer.clear(HttpRequest.request().withPath("${domainPath()}/.*"))

    protected fun verifyRequest(method: Method, path: String, times: Int) {
        mockServer.verify(
            HttpRequest.request()
                .withMethod(method.name)
                .withPath(path),
            VerificationTimes.exactly(times)
        )
    }
}
```

### Rules

- External services must never be called directly.
- Always mock HTTP interactions.
- Verify request count when behavior matters.

---

## Infrastructure Dependencies (Testcontainers)

Use Testcontainers for:

- PostgreSQL
- Redis
- Message brokers

### Rules

- Containers must be shared across tests when possible.
- Database state must be cleaned between tests.
- Never use embedded databases for integration tests.

---

## Common Pitfalls

- Mixing unit and integration concerns in the same test.
- Starting Spring context in unit tests.
- Not resetting MockServer between tests.
- Not cleaning database state.
- Testing implementation details instead of behavior.
- Skipping edge cases.
- Using comments like `// Given`, `// When`, `// Then`.
