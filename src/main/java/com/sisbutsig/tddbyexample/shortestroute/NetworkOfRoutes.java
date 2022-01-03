package com.sisbutsig.tddbyexample.shortestroute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class NetworkOfRoutes {
  private Map<String, Collection<Leg>> legsByFrom = new HashMap<>();

  public NetworkOfRoutes add(Leg leg) {
    Objects.requireNonNull(leg, "leg is null.");
    Collection<Leg> legsForFrom = legsByFrom.computeIfAbsent(leg.getFrom(),
        key -> new ArrayList<>());
    boolean isRedundant = legsForFrom.stream()
                                     .map(Leg::getTo)
                                     .anyMatch(leg.getTo()::equals);
    if (isRedundant) {
      throw new IllegalStateException("Redundant leg " + leg);
    }
    legsForFrom.add(leg);
    return this;
  }

  public int size() {
    return legsByFrom.entrySet()
                     .stream()
                     .map(Entry::getValue)
                     .map(Collection::size)
                     .reduce(0, Integer::sum);
  }
}
