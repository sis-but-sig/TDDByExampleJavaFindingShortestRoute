package com.sisbutsig.tddbyexample.shortestroute;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class GivenALeg {
  @Test
  public void ofShouldCreateAndReturnLegInstance() {
    Leg leg = Leg.of("A", "B", 1.0);
    assertThat(leg.getFrom(), is("A"));
    assertThat(leg.getTo(), is("B"));
    assertThat(leg.getValue(), is(1.0));
  }

  @Test
  public void ofShouldThrowExceptionIfFromIsNull() {
    try {
      Leg.of(null, "B", 1.0);
      fail("Exception expected");
    } catch (NullPointerException e) {
      assertThat(e.getMessage(), is("from is null or empty."));
    }
  }

  @Test
  public void ofShouldThrowExceptionIfFromIsEmpty() {
    try {
      Leg.of("", "B", 1.0);
      fail("Exception expected");
    } catch (NullPointerException e) {
      assertThat(e.getMessage(), is("from is null or empty."));
    }
  }

  @Test
  public void ofShouldThrowExceptionIfFromIsAllSpaces() {
    try {
      Leg.of("   ", "B", 1.0);
      fail("Exception expected");
    } catch (NullPointerException e) {
      assertThat(e.getMessage(), is("from is null or empty."));
    }
  }

  @Test
  public void ofShouldThrowExceptionIfToIsNull() {
    try {
      Leg.of("A", null, 1.0);
      fail("Exception expected");
    } catch (NullPointerException e) {
      assertThat(e.getMessage(), is("to is null or empty."));
    }
  }

  @Test
  public void ofShouldThrowExceptionIfToIsEmpty() {
    try {
      Leg.of("A", "", 1.0);
      fail("Exception expected");
    } catch (NullPointerException e) {
      assertThat(e.getMessage(), is("to is null or empty."));
    }
  }

  @Test
  public void ofShouldThrowExceptionIfToIsAllSpaces() {
    try {
      Leg.of("A", "	", 1.0);
      fail("Exception expected");
    } catch (NullPointerException e) {
      assertThat(e.getMessage(), is("to is null or empty."));
    }
  }

  @Test
  public void ofShouldThrowExceptionIfValueIsLessThanOne() {
    try {
      Leg.of("A", "B", 0);
      fail("Exception expected");
    } catch (IllegalStateException e) {
      assertThat(e.getMessage(), is("value is less than one."));
    }
  }
}
