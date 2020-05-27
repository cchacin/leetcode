package leetcode;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Stack;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.*;

class Parenthesis implements WithAssertions {

    static Stream<Arguments> arguments() {
        return Stream.of(
                of("((()))", "((()))"),
                of("((()))()", "((()))()")
        );
    }

    @ParameterizedTest(name = "balanced({0}) should return {1}")
    @MethodSource("arguments")
    void test(
            String input,
            String expected) {
        assertThat(balanced(input)).isEqualTo(expected);
    }

    // TODO incomplete
    String balanced(
            String input) {
        StringBuilder result = new StringBuilder();

        Stack<Character> stack = new Stack<>();
        char[] chars = input.toCharArray();

        for (char aChar : chars) {
            if (aChar == '(') {
                stack.push(aChar);
                result.append(aChar);
            }
            if (aChar == ')') { // aChar == ')' && open > 0
                if (!stack.isEmpty() && stack.peek() == '(') {
                    result.append(aChar);
                    stack.pop();
//                    if (!stack.isEmpty()) {
//                        result.append(aChar);
//                    }
                } else {
                    stack.push(aChar);
                }
            }
        }
        return result.toString();
    }
}
