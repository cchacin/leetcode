package leetcode;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

class ListsIteratorTest
        implements WithAssertions {
    static Stream<Arguments> arguments() {
        return Stream.of(of(List.of(1, 2, 3), List.of(2, 1, 0), List.of(1, 1, 0)), // normal case, same sizes
                of(List.of(0, 0, 0), List.of(-1, -1, -1), List.of(-1, -1, -1)), // normal case, same sizes
                of(List.of(), List.of(), List.of()), // normal case, empty
                of(List.of(1), List.of(2), List.of(1)), // normal case, 1 element
                of(List.of(1, 3), List.of(2), List.of(1, 3)), // different sizes
                of(List.of(1), List.of(), List.of(1)), // normal case, 1 element
                of(List.of(1), List.of(2, 3), List.of(1, 3)) // normal case, 1 element
        );
    }

    @ParameterizedTest(name = "next({0} {1}) should return {2}")
    @MethodSource("arguments")
    void test(
            final List<Integer> list1,
            final List<Integer> list2,
            final List<Integer> expected) {
        final var iterator = new ListsIterator(list1, list2);
        final var expectedIterator = expected.iterator();
        while (iterator.hasNext()) {
            assertThat(iterator.next()).isEqualTo(expectedIterator.next());
        }
    }

    // - Tienes una interfaz de iterator, y un par de listas.
    // Escribe un iterator que cumpla con la interfaz que devuelva
    // el número más pequeño que hay en las dos listas.
    static class ListsIterator
            implements Iterator<Integer> {

        private final List<Integer> list1;
        private final List<Integer> list2;

        private int index = 0;

        public ListsIterator(
                final List<Integer> list1,
                final List<Integer> list2) {
            this.list1 = Objects.requireNonNull(list1);
            this.list2 = Objects.requireNonNull(list2);
        }

        @Override
        public boolean hasNext() {
            return index < Math.max(list1.size(), list2.size());
        }

        @Override
        public Integer next() {
            final var isList1InBound = index < list1.size();
            final var isList2InBound = index < list2.size();
            final Integer result;
            if (isList1InBound && isList2InBound) {
                result = Math.min(list1.get(index), list2.get(index));
            } else {
                result = (isList1InBound) ? list1.get(index) : list2.get(index);
            }
            index++;
            return result;
        }
    }
}
