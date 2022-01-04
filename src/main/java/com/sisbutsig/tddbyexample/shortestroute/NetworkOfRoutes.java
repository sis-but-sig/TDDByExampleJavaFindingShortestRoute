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
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Stream;

public class NetworkOfRoutes {
  private Map<String, Collection<Leg>> legsByFrom = new HashMap<>();

  public NetworkOfRoutes add(Leg leg) {
    Objects.requireNonNull(leg, "leg is null.");
    Collection<Leg> legsForFrom = legsByFrom.merge(leg.getFrom(),
        new ArrayList<>(), (prev, curr) -> checkRedundancy(prev, leg));
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
    Route shortestRoute = findShortestRoute(from, to, new Stack<>());
    return shortestRoute.legs;
  }

  private Collection<Leg> checkRedundancy(Collection<Leg> legs, Leg leg) {
    if (legs.stream()
            .map(Leg::getTo)
            .anyMatch(leg.getTo()::equals)) {
      throw new IllegalStateException("Redundant leg " + leg);
    }
    return legs;
  }

  private Route findShortestRoute(String from, String to, Stack<String> path) {
    Route route = Route.DEAD_END;
    if (!path.contains(from)) {
      path.push(from);
      route = legsByFrom.getOrDefault(from, emptyList())
                        .stream()
                        .map(findShortest(to, path))
                        .reduce(Route.DEAD_END, Route::shortest);
      path.pop();
    }
    return route;
  }

  private Function<Leg, Route> findShortest(String to, Stack<String> path) {
    return leg -> leg.getTo()
                     .equals(to) ? Route.of(leg)
                         : Route.of(leg,
                             findShortestRoute(leg.getTo(), to, path));
  }

  private static class Route {
    private static final Route DEAD_END = new Route(emptyList(),
        Double.POSITIVE_INFINITY);

    private Collection<Leg> legs;
    private double totalValue;

    private Route(Collection<Leg> legs, double totalValue) {
      this.legs = legs;
      this.totalValue = totalValue;
    }

    private static Route of(Leg leg, Route subroute) {
      return subroute.isDeadEnd() ? DEAD_END
          : new Route(
              concat(Stream.of(leg), subroute.legs.stream()).collect(toList()),
              leg.getValue() + subroute.totalValue);
    }

    private static Route of(Leg leg) {
      return new Route(singletonList(leg), leg.getValue());
    }

    private static Route shortest(Route route1, Route route2) {
      return route1.totalValue < route2.totalValue ? route1 : route2;
    }

    private boolean isDeadEnd() {
      return legs.isEmpty();
    }
  }
}
