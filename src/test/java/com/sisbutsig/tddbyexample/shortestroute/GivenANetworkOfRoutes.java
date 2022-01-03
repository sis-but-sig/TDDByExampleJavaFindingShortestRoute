package com.sisbutsig.tddbyexample.shortestroute;

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
}
