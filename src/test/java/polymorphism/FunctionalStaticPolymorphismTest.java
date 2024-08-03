package polymorphism;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FunctionalStaticPolymorphismTest {

    private static final String TEXT = "My Money Mom Mine  Model my mom";
    private static final String TEXT_2 = "My Money Mom Mine Model my mom  Merry  Man Menu MONEY  MeNu";

    private static final String RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED = "The result map should match the expected map";

    @Test
    @Order(1)
    @DisplayName("wordsAmount methods should return Map<String, Integer>")
    void wordsAmountReturnsMapTest() {
        Method[] declaredMethods = FunctionalStaticPolymorphism.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.getName().equals("wordsAmount")) {
                assertEquals(Map.class, method.getReturnType(), "The result should be an instance of Map<String, Integer>");
            }
        }
    }


    @Order(5)
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideArgsForWords1")
    @DisplayName("wordsAmount(String text) method test")
    void wordsAmountTest(BiConsumer<Map<String, Integer>, Map<String, Integer>> assertion, String text, Map<String, Integer> expectedResult) {
        try {
            Map<String, Integer> actualResult = (Map<String, Integer>) FunctionalStaticPolymorphism.wordsAmount(text);

            assertion.accept(actualResult, expectedResult);
        } catch (ClassCastException e) {
            System.out.println("The result should be an instance of Map<String, Integer>");
        }
    }

    private static Stream<Arguments> provideArgsForWords1() {
        return Stream.of(
                Arguments.of(Named.named("Result should ignore whitespaces",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertFalse(actual.containsKey("") || actual.containsKey(" "),
                                                "The result map should not contain empty strings or whitespaces"
                                        )
                        ), TEXT, Collections.emptyMap()
                ),
                Arguments.of(
                        Named.named("Words. [My Money Mom Mine Model my mom] -> {mine=1, money=1, model=1, mom=2, my=2}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED)
                        ),
                        TEXT, Map.of("mine", 1, "money", 1, "model", 1, "mom", 2, "my", 2)
                ),
                Arguments.of(
                        Named.named("Words. [My Money Mom Mine Model my mom  Merry  Man Menu MONEY  MeNu] -> {mine=1, money=2, model=1, mom=2, man=1, menu=2, my=2, merry=1}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED)
                        ),
                        TEXT_2, Map.of("mine", 1, "money", 2, "model", 1, "mom", 2, "man", 1, "menu", 2, "my", 2, "merry", 1)
                )
        );
    }

    @Order(10)
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideArgsForWords2")
    @DisplayName("wordsAmount(String text, int minLength, int maxLength) method test")
    void wordsAmountWithLengthTest(BiConsumer<Map<String, Integer>, Map<String, Integer>> assertion, String text, int minLength, int maxLength, Map<String, Integer> expectedResult) {
        try {
            Map<String, Integer> actualResult = (Map<String, Integer>) FunctionalStaticPolymorphism.wordsAmount(text, minLength, maxLength);
            assertion.accept(actualResult, expectedResult);
        } catch (ClassCastException e) {
            System.out.println("The result should be an instance of Map<String, Integer>");
        }
    }

    private static Stream<Arguments> provideArgsForWords2() {
        return Stream.of(
                Arguments.of(Named.named("Result should ignore whitespaces",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertFalse(actual.containsKey("") || actual.containsKey(" "),
                                                "The result map should not contain empty strings or whitespaces")
                        ),
                        TEXT, 0, 10000,
                        Collections.emptyMap()
                ),
                Arguments.of(Named.named("Words with length between 0 and 10000. [My Money Mom Mine Model my mom] -> {mom=2, mine=1, model=1, money=1, my=2}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED)
                        ),
                        TEXT, 0, 10000,
                        Map.of("mom", 2, "mine", 1, "model", 1, "money", 1, "my", 2)
                ),
                Arguments.of(
                        Named.named("Words with length between 3 and 4. [My Money Mom Mine Model my mom] -> {mom=2, mine=1}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED)
                        ),
                        TEXT, 3, 4,
                        Map.of("mom", 2, "mine", 1)
                ),
                Arguments.of(
                        Named.named("Words with length between 3 and 4. [My Money Mom Mine Model my mom  Merry  Man Menu MONEY  MeNu] -> {mom=2, mine=1, man=1, menu=2}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED)
                        ),
                        TEXT_2, 3, 4,
                        Map.of("mom", 2, "mine", 1, "man", 1, "menu", 2)
                )
        );
    }


    @Order(15)
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideArgsForWords3")
    @DisplayName("wordsAmount(String text, int minLength, int maxLength, String startWith) method test")
    void wordsAmountWithLengthAndStartWithTest(BiConsumer<Map<String, Integer>, Map<String, Integer>> assertion, String text, int minLength, int maxLength, String startWith, Map<String, Integer> expectedResult) {
        try {
            Map<String, Integer> actualResult = (Map<String, Integer>) FunctionalStaticPolymorphism.wordsAmount(text, minLength, maxLength, startWith);
            assertion.accept(actualResult, expectedResult);
        } catch (ClassCastException e) {
            System.out.println("The result should be an instance of Map<String, Integer>");
        }
    }

    private static Stream<Arguments> provideArgsForWords3() {
        return Stream.of(
                Arguments.of(Named.named("Result should ignore whitespaces",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertFalse(actual.containsKey("") || actual.containsKey(" "),
                                                "The result map should not contain empty strings or whitespaces")
                        ),
                        TEXT, 0, 10000, null,
                        Collections.emptyMap()
                ),
                Arguments.of(
                        Named.named("Words with length between 3 and 4 starting with <null>. [My Money Mom Mine Model my mom] -> {mom=2, mine=1}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) -> {
                                    assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED);
                                }
                        ),
                        TEXT, 3, 4, null,
                        Map.of("mom", 2, "mine", 1)
                ),
                Arguments.of(
                        Named.named("Words with length between 3 and 4 starting with ''. [My Money Mom Mine Model my mom] -> {mom=2, mine=1}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) -> {
                                    assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED);
                                }
                        ),
                        TEXT, 3, 4, "",
                        Map.of("mom", 2, "mine", 1)
                ),
                Arguments.of(
                        Named.named("Words with length between 3 and 4 starting with 'MP'. [My Money Mom Mine Model my mom] -> {}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) -> {
                                    assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED);
                                }
                        ),
                        TEXT, 3, 4, "MP",
                        Collections.emptyMap()
                ),
                Arguments.of(
                        Named.named("Words with length between 3 and 4 starting with 'mo'. [My Money Mom Mine Model my mom] -> {mom=2}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) -> {
                                    assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED);
                                }
                        ),
                        "My Money Mom Mine Model my mom",
                        3, 4, "mo",
                        Map.of("mom", 2)
                ),
                Arguments.of(
                        Named.named("Words with length between 3 and 4 starting with 'mo'. [My Money Mom Mine Model my mom] -> {mom=2, model=1, money=1}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) -> {
                                    assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED);
                                }
                        ),
                        TEXT, 3, 10, "mo",
                        Map.of("mom", 2, "model", 1, "money", 1)
                ),
                Arguments.of(
                        Named.named("Words with length between 3 and 10 starting with 'mE'. [My Money Mom Mine Model my mom Merry Man Menu MONEY MeNu] -> {menu=2, merry=1}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) -> {
                                    assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED);
                                }
                        ),
                        TEXT_2, 3, 10, "mE",
                        Map.of("menu", 2, "merry", 1)
                )
        );
    }


    @Order(20)
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideArgsForWords4")
    @DisplayName("wordsAmount(String text, Predicate<String>... predicates) method test")
    void wordsAmountWithPredicatesTest(BiConsumer<Map<String, Integer>, Map<String, Integer>> assertion, String text, Predicate<String>[] predicates, Map<String, Integer> expectedResult) {
        try {
            Map<String, Integer> actualResult = (Map<String, Integer>) FunctionalStaticPolymorphism.wordsAmount(text, predicates);
            assertion.accept(actualResult, expectedResult);
        } catch (ClassCastException e) {
            System.out.println("The result should be an instance of Map<String, Integer>");
        }
    }

    private static Stream<Arguments> provideArgsForWords4() {
        return Stream.of(
                Arguments.of(Named.named("Result should ignore whitespaces",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertFalse(actual.containsKey("") || actual.containsKey(" "),
                                                "The result map should not contain empty strings or whitespaces"
                                        )
                        ), TEXT, null,
                        Collections.emptyMap()
                ),
                Arguments.of(
                        Named.named("Words with predicates 's -> s.length() > 3', 's -> s.endsWith(y)'. [] -> {}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED)
                        ),
                        "",
                        new Predicate[]{(Predicate<String>) s -> s.length() > 3, (Predicate<String>) s -> s.endsWith("y")},
                        Collections.emptyMap()
                ),
                Arguments.of(
                        Named.named("Words with predicates <null>. [My Money Mom Mine Model my mom] -> {mine=1, money=1, model=1, mom=2, my=2}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED)
                        ),
                        TEXT, null,
                        Map.of("mine", 1, "money", 1, "model", 1, "mom", 2, "my", 2)
                ),
                Arguments.of(
                        Named.named("Words with predicates 's -> s.equalsIgnoreCase(HEllO)'. [My Money Mom Mine Model my mom] -> {}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED)
                        ),
                        TEXT,
                        new Predicate[]{(Predicate<String>) s -> s.equalsIgnoreCase("HEllO")},
                        Collections.emptyMap()
                ),
                Arguments.of(
                        Named.named("Words with predicates 's -> s.endsWith(y)'. [My Money Mom Mine Model my mom] -> {money=1, my=2}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED)
                        ),
                        TEXT,
                        new Predicate[]{(Predicate<String>) s -> s.endsWith("y")},
                        Map.of("money", 1, "my", 2)
                ),
                Arguments.of(
                        Named.named("Words with predicates 's -> s.length() > 3', 's -> s.endsWith(y)'. [My Money Mom Mine Model my mom  Merry  Man Menu MONEY  MeNu] -> {money=2, merry=1}",
                                (BiConsumer<Map<String, Integer>, Map<String, Integer>>) (actual, expected) ->
                                        assertEquals(expected, actual, RES_MAP_SHOULD_BE_EQUALS_TO_EXPECTED)
                        ),
                        TEXT_2,
                        new Predicate[]{(Predicate<String>) s -> s.length() > 3, (Predicate<String>) s -> s.endsWith("y")},
                        Map.of("money", 2, "merry", 1)
                )
        );
    }
}
