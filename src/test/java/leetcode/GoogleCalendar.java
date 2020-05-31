package leetcode;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;
import static org.junit.jupiter.params.provider.Arguments.of;

class GoogleCalendar implements WithAssertions {

    /**
     * # Sample input:
     * # [['9:00', '10:30'], ['12:00', '13:00'], ['16:00', '18:00']]
     * # ['9:00', '20:00']
     * # [['10:00', '11:30'], ['12:30', '14:30'], ['14:30', '15:00'], ['16:00', '17:00']]
     * # ['10:00', '18:30']
     * # 30
     * # Sample output: [['11:30', '12:00'], ['15:00', '16:00'], ['18:00', '18:30']]
     */
    static Stream<Arguments> arguments() {
        return Stream.of(
                of(
                        Calendar.of(
                                List.of(
                                        Slot.from(Time.of(9), Time.of(10, 30)),
                                        Slot.from(Time.of(12), Time.of(13)),
                                        Slot.from(Time.of(16), Time.of(18))
                                ),
                                Slot.from(Time.of(9), Time.of(20))),
                        Calendar.of(
                                List.of(
                                        Slot.from(Time.of(10), Time.of(11, 30)),
                                        Slot.from(Time.of(12, 30), Time.of(14, 30)),
                                        Slot.from(Time.of(14, 30), Time.of(15)),
                                        Slot.from(Time.of(16), Time.of(17))
                                ),
                                Slot.from(Time.of(10), Time.of(18, 30))),
                        Time.of(0, 30),
                        List.of(
                                Slot.from(Time.of(11, 30), Time.of(12)),
                                Slot.from(Time.of(15), Time.of(16)),
                                Slot.from(Time.of(18), Time.of(18, 30))
                        )
                )
        );
    }

    @ParameterizedTest(name = "maxProfit({0}) should return {1}")
    @MethodSource("arguments")
    void test(
            Calendar one,
            Calendar two,
            Time duration,
            List<Slot> expected) {
        List<Slot> availability = one.availability(two, duration);
        System.out.println("availability = " + availability);
        assertThat(availability).containsExactlyElementsOf(expected);
    }

    static record Time(int hour, int minutes) {
        static Time of(int hour, int minutes) {
            return new Time(hour, minutes);
        }

        static Time of(int hour) {
            return new Time(hour, 0);
        }

        static Time fromIndex(int index) {
            var bd = new BigDecimal(index / 60);
            var intPart = bd.intValue();
            var decimalPart = index % 60;
            return Time.of(intPart, decimalPart);
        }

        int toIndex() {
            return (hour * 60) + minutes;
        }

        @Override
        public String toString() {
            return String.format("%d:%02d", hour, minutes);
        }
    }

    static record Slot(Time start, Time end) {
        static Slot from(
                Time start,
                Time end) {
            return new Slot(start, end);
        }
        int duration() {
            return end.toIndex() - start.toIndex();
        }

        @Override
        public String toString() {
            return String.format("[%s - %s]", start, end);
        }
    }

    static record Calendar(List<Slot>meetings, Slot bounds) {

        static Calendar of(
                List<Slot>meetings,
                Slot bounds) {
            return new Calendar(meetings, bounds);
        }

        List<Slot> availableSlots() {
            var availableSlots = new ArrayList<Slot>();
            var firstMeeting = meetings.get(0);
            if (bounds.start.toIndex() < firstMeeting.start.toIndex()) {
                availableSlots.add(Slot.from(bounds.start, firstMeeting.start));
            }

            range(0, meetings.size() - 1)
                    .mapToObj(i -> Slot.from(meetings.get(i).end, meetings.get(i + 1).start))
                    .filter(slot -> slot.duration() > 0)
                    .forEach(availableSlots::add);

            var lastMeeting = meetings.get(meetings.size() - 1);
            if (bounds.end.toIndex() > lastMeeting.end.toIndex()) {
                availableSlots.add(Slot.from(lastMeeting.end, bounds.end));
            }

            return availableSlots;
        }

        List<Slot> availability(Calendar calendar, Time duration) {
            System.out.println(this.availableSlots());
            System.out.println(this.meetings);
            System.out.println(calendar.availableSlots());
            System.out.println(calendar.meetings);

            var minutesPerDay = new int[1440];

            this.availableSlots().forEach(slot ->
                    range(slot.start.toIndex(), slot.end.toIndex())
                            .forEach(i -> minutesPerDay[i] = 1));

            var start = new AtomicInteger(-1);
            var result = new ArrayList<Slot>();
            calendar.availableSlots().forEach(slot ->
                    range(slot.start.toIndex(), slot.end.toIndex())
                            .forEach(i -> {
                                if (minutesPerDay[i] == 1) {
                                    minutesPerDay[i] = 3;
                                } else {
                                    minutesPerDay[i] = 2;
                                    if (start.intValue() != -1) {
                                        var newSlot = Slot.from(
                                                Time.fromIndex(start.intValue()),
                                                Time.fromIndex(i)
                                        );
                                        if (newSlot.duration() >= duration.toIndex()) {
                                            result.add(newSlot);
                                        }
                                        start.set(-1);
                                    }
                                }

                            }));

            range(0, minutesPerDay.length)
                    .forEach(i -> {
                        if (minutesPerDay[i] == 3) {
                            if (start.intValue() == -1) {
                                start.set(i);
                            }
                        } else if (start.intValue() != -1) {
                            var newSlot = Slot.from(
                                    Time.fromIndex(start.intValue()),
                                    Time.fromIndex(i)
                            );
                            if (newSlot.duration() >= duration.toIndex()) {
                                result.add(newSlot);
                            }
                            start.set(-1);
                        }
                    });
            return result;
        }
    }

}
