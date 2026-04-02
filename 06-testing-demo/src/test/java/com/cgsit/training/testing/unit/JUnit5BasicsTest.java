package com.cgsit.training.testing.unit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Basics — all important features in one file.
 *
 * Run:  mvn test -Dtest=JUnit5BasicsTest
 *       or right-click in IntelliJ → Run
 */
@DisplayName("JUnit 5 Basics")
class JUnit5BasicsTest {

    // ========== LIFECYCLE ==========

    @BeforeAll
    static void beforeAll() {
        System.out.println(">>> @BeforeAll — runs once before all tests");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("  >>> @BeforeEach — runs before EACH test");
    }

    @AfterEach
    void afterEach() {
        System.out.println("  <<< @AfterEach — runs after EACH test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("<<< @AfterAll — runs once after all tests");
    }

    // ========== BASIC ASSERTIONS ==========

    @Test
    @DisplayName("assertEquals — compare values")
    void testEquals() {
        assertEquals(4, 2 + 2);
        assertEquals("Hello", "Hello");
        assertEquals(3.14, Math.PI, 0.01);  // delta for floating point
    }

    @Test
    @DisplayName("assertTrue / assertFalse — check conditions")
    void testBoolean() {
        assertTrue(10 > 5);
        assertFalse("Hello".isEmpty());
        assertTrue(List.of(1, 2, 3).contains(2));
    }

    @Test
    @DisplayName("assertNotNull / assertNull — null checks")
    void testNull() {
        assertNotNull("I exist");
        assertNull(null);
        assertNotNull(List.of());  // empty list is not null
    }

    @Test
    @DisplayName("assertSame — same object reference")
    void testSame() {
        String a = "Hello";
        String b = a;
        assertSame(a, b);        // same reference
        assertNotSame(a, new String("Hello"));  // different object
    }

    // ========== ASSERTTHROWS ==========

    @Test
    @DisplayName("assertThrows — verify exception is thrown")
    void testException() {
        assertThrows(ArithmeticException.class, () -> {
            int result = 10 / 0;
        });
    }

    @Test
    @DisplayName("assertThrows — check exception message")
    void testExceptionMessage() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("Price must be positive");
        });
        assertEquals("Price must be positive", ex.getMessage());
        assertTrue(ex.getMessage().contains("positive"));
    }

    @Test
    @DisplayName("assertDoesNotThrow — verify no exception")
    void testNoException() {
        assertDoesNotThrow(() -> Integer.parseInt("42"));
    }

    // ========== ASSERTALL ==========

    @Test
    @DisplayName("assertAll — check multiple things at once")
    void testAssertAll() {
        String name = "Keyboard";
        double price = 89.99;

        // All assertions run, even if one fails — shows ALL failures
        assertAll("product validation",
            () -> assertNotNull(name),
            () -> assertFalse(name.isBlank()),
            () -> assertTrue(name.length() <= 100),
            () -> assertTrue(price > 0)
        );
    }

    // ========== ASSERTTIMEOUT ==========

    @Test
    @DisplayName("assertTimeout — verify execution time")
    void testTimeout() {
        assertTimeout(Duration.ofMillis(500), () -> {
            // this should finish in under 500ms
            Thread.sleep(50);
        });
    }

    // ========== @DISABLED ==========

    @Test
    @Disabled("Temporarily disabled — fix in next sprint")
    @DisplayName("This test is skipped")
    void skippedTest() {
        fail("This should not run");
    }

    // ========== @PARAMETERIZEDTEST ==========

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8, 13})
    @DisplayName("Positive numbers are greater than zero")
    void testPositiveNumbers(int number) {
        assertTrue(number > 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Keyboard", "Mouse", "Monitor"})
    @DisplayName("Product names are not blank")
    void testProductNames(String name) {
        assertFalse(name.isBlank());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Null and empty strings are invalid")
    void testInvalidStrings(String value) {
        assertTrue(value == null || value.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
        "10, 20, 30",
        "0, 0, 0",
        "-1, 1, 0",
        "100, 200, 300"
    })
    @DisplayName("Addition with CSV data")
    void testAddition(int a, int b, int expected) {
        assertEquals(expected, a + b);
    }

    @ParameterizedTest
    @CsvSource({
        "Keyboard, 89.99",
        "Mouse, 29.99",
        "Monitor, 349.00"
    })
    @DisplayName("Product name and price from CSV")
    void testProductData(String name, double price) {
        assertFalse(name.isBlank());
        assertTrue(price > 0);
    }

    // ========== @CsvFileSource ==========
    //
    // Loads test data from a CSV file in src/test/resources/.
    //
    // Important parameters:
    //   encoding       — file encoding (default: platform-dependent!)
    //                    ALWAYS set to "UTF-8" for portable tests
    //   numLinesToSkip  — skip header lines
    //   delimiter       — column separator (default: ',')
    //   delimiterString — multi-char separator (e.g. "\t" for tab)
    //   nullValues      — which strings become null (e.g. "N/A", "")

    @ParameterizedTest
    @CsvFileSource(
        resources = "/test-products.csv",
        numLinesToSkip = 1,
        encoding = "UTF-8"          // explicit! default is platform-dependent
    )
    @DisplayName("Product data from CSV file (UTF-8, comma-separated)")
    void testFromCsvFile(String name, double price, String category) {
        assertFalse(name.isBlank(), "Name should not be blank: " + name);
        assertTrue(price > 0, "Price should be positive: " + price);
        assertFalse(category.isBlank(), "Category should not be blank");
    }

    @ParameterizedTest
    @CsvFileSource(
        resources = "/produkte-deutsch.csv",
        numLinesToSkip = 1,
        encoding = "UTF-8",         // Umlaute: ü, ö, ä
        delimiter = ';',            // European CSV: semicolon instead of comma
        nullValues = {"N/A", ""}    // empty string and "N/A" become null
    )
    @DisplayName("German CSV with semicolons, Umlaute, and null values")
    void testGermanCsvFile(String name, Double price, String category, String description) {
        if (name != null) {
            assertFalse(name.isBlank());
        }
        // price and category can be null (nullValues = {"N/A", ""})
        if (price != null) {
            assertTrue(price > 0);
        }
    }

    // ========== @EnumSource ==========

    enum Status { ACTIVE, INACTIVE, PENDING }

    @ParameterizedTest
    @EnumSource(Status.class)
    @DisplayName("All enum values are not null")
    void testAllEnumValues(Status status) {
        assertNotNull(status);
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"ACTIVE", "PENDING"})
    @DisplayName("Only selected enum values")
    void testSelectedEnumValues(Status status) {
        assertNotEquals(Status.INACTIVE, status);
    }

    // ========== @MethodSource ==========

    @ParameterizedTest
    @MethodSource("provideProductArguments")
    @DisplayName("Product data from method source")
    void testFromMethodSource(String name, double price) {
        assertFalse(name.isBlank());
        assertTrue(price > 0);
    }

    static Stream<Arguments> provideProductArguments() {
        return Stream.of(
            Arguments.of("Keyboard", 89.99),
            Arguments.of("Mouse", 29.99),
            Arguments.of("Monitor", 349.00),
            Arguments.of("Headset", 129.00)
        );
    }

    // ========== @NESTED ==========

    @Nested
    @DisplayName("String operations")
    class StringTests {

        @Test
        @DisplayName("toUpperCase works")
        void testUpperCase() {
            assertEquals("HELLO", "hello".toUpperCase());
        }

        @Test
        @DisplayName("contains checks substring")
        void testContains() {
            assertTrue("Hello World".contains("World"));
        }

        @Test
        @DisplayName("trim removes whitespace")
        void testTrim() {
            assertEquals("Hello", "  Hello  ".trim());
        }
    }

    @Nested
    @DisplayName("List operations")
    class ListTests {

        @Test
        @DisplayName("List.of creates immutable list")
        void testImmutableList() {
            var list = List.of("A", "B", "C");
            assertEquals(3, list.size());
            assertThrows(UnsupportedOperationException.class,
                () -> list.add("D"));
        }

        @Test
        @DisplayName("Empty list has size 0")
        void testEmptyList() {
            assertTrue(List.of().isEmpty());
            assertEquals(0, List.of().size());
        }
    }
}
