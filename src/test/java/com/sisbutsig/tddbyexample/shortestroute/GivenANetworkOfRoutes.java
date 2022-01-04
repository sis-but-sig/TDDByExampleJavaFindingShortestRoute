package com.sisbutsig.tddbyexample.shortestroute;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class GivenANetworkOfRoutes {
  @Test
  public void addShouldAddLegToTheNetwork() {
    NetworkOfRoutes routes = new NetworkOfRoutes();
    routes.add(Leg.of("A", "B", 1.0));

    assertThat(routes.size(), is(1));
  }

  @Test
  public void addShouldBeFluent() {
    NetworkOfRoutes routes = new NetworkOfRoutes().add(Leg.of("A", "B", 1.0))
                                                  .add(Leg.of("B", "C", 1.0));

    assertThat(routes.size(), is(2));
  }

  @Test
  public void addShouldThrowExceptionIfLegIsNull() {
    try {
      new NetworkOfRoutes().add(null);
      fail("Exception expected.");
    } catch (NullPointerException e) {
      assertThat(e.getMessage(), is("leg is null."));
    }
  }

  @Test
  public void addShouldDisallowRedundantLegs() {
    try {
      new NetworkOfRoutes().add(Leg.of("A", "B", 1.0))
                           .add(Leg.of("A", "B", 2.0));
      fail("Exception expected.");
    } catch (IllegalStateException e) {
      assertThat(e.getMessage(),
          is("Redundant leg Leg [from=A, to=B, value=2.0]"));
    }
  }

  @Test
  public void findShortestShouldWorkAsExpected() {
    Leg legAB = Leg.of("A", "B", 1.0);
    Leg legBC = Leg.of("B", "C", 2.0);
    Leg legCD = Leg.of("C", "D", 1.0);
    Leg legAC = Leg.of("A", "C", 2.0);
    NetworkOfRoutes routes = new NetworkOfRoutes().add(legAB)
                                                  .add(legBC)
                                                  .add(legCD)
                                                  .add(legAC);

    // A to B: [A --1--> B]
    assertThat(routes.findShortest("A", "B"), contains(legAB));
    // B to C: [B --2--> C]
    assertThat(routes.findShortest("B", "C"), contains(legBC));
    // C to D: [C --1--> D]
    assertThat(routes.findShortest("C", "D"), contains(legCD));
    // B to D: [B --2--> C, C --1--> D]
    assertThat(routes.findShortest("B", "D"), contains(legBC, legCD));
    // A to C: [A --2--> C]
    assertThat(routes.findShortest("A", "C"), contains(legAC));
    // A to D: [A --2--> C, C --1--> D]
    assertThat(routes.findShortest("A", "D"), contains(legAC, legCD));
    // A to E: []
    assertThat(routes.findShortest("A", "E"), empty());
  }
}
