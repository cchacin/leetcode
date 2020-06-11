package leetcode;

import org.assertj.core.api.WithAssertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static leetcode.SplitWiseAppTest.Invoice.newInvoice;
import static leetcode.SplitWiseAppTest.Item.newItem;
import static leetcode.SplitWiseAppTest.Result.newResult;
import static org.junit.jupiter.params.provider.Arguments.of;

/*
You're eating out with Jack Dorsey and his evil twin Jack Horsey,
and all three of you decide to very fairly split the bill.

The solution of this problem is similar to a real online service called Splitwise,
which allows groups to post group expenses to split among the group.

Katsu curry, $13.00 - Jack Dorsey
Duck ramen, $15.50 - Carlos
Croquettes, $3.00 - Jack Dorsey

Jack Dorsey, $16.00
- Katsu curry
- Croquettes
Carlos, $15.50
- Duck ramen


Daniel: $20 sushi
Carlos: $10 sandwish
-- subtotal: $30
tax: $2
tip: $4

Daniel, $24.00
  - Sushi
Carlos, $12.00
  - Sandwish
 */
class SplitWiseAppTest implements WithAssertions {

    private static final CurrencyUnit USD = Monetary.getCurrency("USD");

    static Stream<Arguments> arguments() {
        return Stream.of(
                of(
                        newInvoice(
                                Map.of(
                                        "Jack Dorsey", List.of(
                                                newItem("Katsu curry", usDollars(13)),
                                                newItem("Croquettes", usDollars(3))
                                        ),
                                        "Carlos", List.of(
                                                newItem("Duck Ramen", usDollars(15.50))
                                        )), 2, 4), List.of(
                                newResult("Jack Dorsey", usDollars(19.05).toString(), "Katsu curry", "Croquettes"),
                                newResult("Carlos", usDollars(18.45).toString(), "Duck Ramen")
                        ),
                        newInvoice(
                                Map.of("Daniel", List.of(
                                        newItem("Sushi", usDollars(20))
                                ), "Carlos", List.of(
                                        newItem("Sandwich", usDollars(10))
                                )), 2, 4),
                        List.of(
                                newResult("Daniel", usDollars(24).toString(), "Sushi"),
                                newResult("Carlos", usDollars(12).toString(), "Sandwich")
                        )
                )
        );
    }

    @ParameterizedTest(name = "myFunction({0}) should return {1}")
    @MethodSource("arguments")
    void test(
            Invoice input,
            List<Result> expected) {
        assertThat(input.calculate()).containsExactlyInAnyOrderElementsOf(expected);
    }

    static Money usDollars(double amount) {
        return Money.of(amount, USD);
    }

    static record Item(
            String name,
            MonetaryAmount price) {
        static Item newItem(
                String name,
                Money price) {
            return new Item(name, price);
        }
    }

    static record Result(
            String name,
            String total,
            List<String>items) {

        static Result newResult(
                String name,
                String total,
                String... items) {
            return new Result(name, total, Arrays.asList(items));
        }

        static Result newResult(
                String name,
                String total,
                List<String> items) {
            return new Result(name, total, items);
        }
    }

    static record Invoice(
            Map<String, List<Item>>rows,
            int tax,
            int tip) {

        static Invoice newInvoice(
                Map<String, List<Item>> rows,
                int tax,
                int tip) {
            return new Invoice(rows, tax, tip);
        }

        List<Result> calculate() {
            var total = rows().entrySet()
                    .stream()
                    .flatMap(entry -> entry.getValue().stream())
                    .map(Item::price)
                    .reduce(MonetaryAmount::add)
                    .orElseGet(() -> usDollars(0));

            return rows().entrySet().stream()
                    .map(entry -> {
                        var totalPerPerson = entry.getValue()
                                .stream()
                                .map(Item::price)
                                .reduce(MonetaryAmount::add)
                                .orElseGet(() -> usDollars(0));

                        return newResult(
                                entry.getKey(),
                                totalPerPerson.add(
                                        Money.of(tax + tip, USD)
                                                .multiply(
                                                        totalPerPerson.divide(total.getNumber()).getNumber()
                                                )
                                ).toString(),
                                entry.getValue()
                                        .stream()
                                        .map(Item::name)
                                        .collect(Collectors.toList()));
                    }).collect(Collectors.toList());
        }
    }
}
