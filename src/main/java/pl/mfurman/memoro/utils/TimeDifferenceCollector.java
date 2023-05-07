package pl.mfurman.memoro.utils;

import pl.mfurman.memoro.dto.TimeTuple;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class TimeDifferenceCollector implements Collector<TimeTuple, List<TimeTuple>, Long> {

  @Override
  public Supplier<List<TimeTuple>> supplier() {
    return ArrayList::new;
  }

  @Override
  public BiConsumer<List<TimeTuple>, TimeTuple> accumulator() {
    return List::add;
  }

  @Override
  public BinaryOperator<List<TimeTuple>> combiner() {
    return (list1, list2) -> {
      list1.addAll(list2);
      return list1;
    };
  }

  @Override
  public Function<List<TimeTuple>, Long> finisher() {
    return list -> {
      if (list.size() > 1) {
        final LocalDateTime firstDate = list.get(0).date();
        final LocalDateTime lastDate = list.get(list.size() - 1).date();
        return Duration.between(firstDate, lastDate).toSeconds();
      } else return 0L;
    };
  }

  @Override
  public Set<Characteristics> characteristics() {
    return EnumSet.noneOf(Characteristics.class);
  }
}
