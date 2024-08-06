package polymorphism;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;

/**
 * Use Static Polymorphism and implement methods that will provide amount of words in 'text' parameter. <br><br>
 * <p>
 * You can run {@link polymorphism.FunctionalStaticPolymorphismTest} to check if you have done it correctly. <br><br>
 *
 * <b>When you implement all methods, revise the implementations and perform deduplication in the code.</b>
 */
@UtilityClass
public class FunctionalStaticPolymorphism {

    /**
     * Provides amount of words in 'text' parameter. <br><br>
     * <pre>
     *     text = "My Money Mom Mine Model my mom"
     *     result = "my=2, money=1, mom=2, mine=1, model=1"
     * </pre>
     * <b>Hint: to collect words you can take a look at the {@link Collectors#summingInt(ToIntFunction)}</b>
     *
     * @return map of words and their amount
     */
    public static Map<String, Integer> wordsAmount(String text) {
        return wordsAmount(text, s -> true);
    }

    /**
     * <b>Use Static Polymorphism</b><br>
     * Provides amount of words in 'text' parameter with 'minLength' and 'maxLength' parameters. <br>
     * <pre>
     *     text = "My Money Mom Mine Model my mom"
     *     minLength = 3
     *     maxLength = 4
     *     result = "mom=2, mine=1"
     * </pre>
     *
     * @param text      text to analyze
     * @param minLength minimum length of word
     * @param maxLength maximum length of word
     * @return map of words and their amount
     */
    public static Map<String, Integer> wordsAmount(String text, Integer minLength, Integer maxLength) {
        return wordsAmount(text,
                s -> s.length() >= minLength,
                s -> s.length() <= maxLength);
    }

    /**
     * <b>Use Static Polymorphism</b><br>
     * Provides amount of words in 'text' parameter with minLength, maxLength and startWith parameters. <br>
     * <pre>
     *     text = "My Money Mom Mine Model my mom"
     *     minLength = 3
     *     maxLength = 4
     *     startWith = "mi"
     *     result = "mine=1"
     * </pre>
     *
     * @param text      text to analyze
     * @param minLength minimum length of word
     * @param maxLength maximum length of word
     * @param startWith word should start with this letter
     * @return map of words and their amount
     */
    public static Map<String, Integer> wordsAmount(String text, Integer minLength, Integer maxLength, String startWith) {
        return wordsAmount(text,
                s -> s.length() >= minLength,
                s -> s.length() <= maxLength,
                s -> startWith == null || s.startsWith(startWith.toLowerCase()));
    }

    /**
     * <b>Use Static Polymorphism</b><br>
     * Provides amount of words in 'text' parameter by Predicates. <br>
     * <pre>
     *     text = "My Money Mom Mine Model my mom"
     *     predicates = [s -> s.startsWith("m"), s -> s.length() > 3]
     *     result = "money=1, mine=1, model=1"
     * </pre>
     *
     * @param text       text to analyze
     * @param predicates predicates to filter words
     * @return map of words and their amount
     */
    public static Map<String, Integer> wordsAmount(String text, Predicate<? super String>... predicates) {
        return Arrays.stream(text.split(" "))
                .filter(not(String::isBlank))
                .map(String::toLowerCase)
                .filter(s -> predicates == null || Arrays.stream(predicates).allMatch(p -> p.test(s)))
                .collect(groupingBy(Function.identity(), Collectors.summingInt(ignored -> 1)));

    }

}
