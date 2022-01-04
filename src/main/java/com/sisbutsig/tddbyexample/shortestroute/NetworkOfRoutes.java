package com.sisbutsig.tddbyexample.shortestroute;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Stream;

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

  public Collection<Leg> findShortest(String from, String to) {
    return findShortestRoute(from, to).legs;
  }

  private Route findShortestRoute(String from, String to) {
    return legsByFrom.getOrDefault(from, emptyList())
                     .stream()
                     .map(leg -> findShortest(leg, to))
                     .reduce(Route.DEAD_END, Route::shortest);
  }

  private Route findShortest(Leg leg, String to) {
    return leg.getTo()
              .equals(to) ? Route.of(leg)
                  : Route.of(leg, findShortestRoute(leg.getTo(), to));
  }

  public static class Route {
    private static final Route DEAD_END = new Route(emptyList());

    private Collection<Leg> legs;
    private double totalValue;

    private Route(Collection<Leg> legs) {
      this.legs = legs;
      totalValue = legs.stream()
                       .map(Leg::getValue)
                       .reduce(0d, Double::sum);
    }

    private static Route of(Leg leg, Route subroute) {
      return subroute.isDeadEnd() ? DEAD_END
          : new Route(
              concat(Stream.of(leg), subroute.legs.stream()).collect(toList()));
    }

    private static Route of(Leg leg) {
      return new Route(singletonList(leg));
    }

    private static Route shortest(Route route1, Route route2) {
      return route1.isDeadEnd() ? route2
          : (route2.isDeadEnd() ? route1
              : (route1.totalValue < route2.totalValue ? route1 : route2));
    }

    private boolean isDeadEnd() {
      return legs.isEmpty();
    }
  }
}
