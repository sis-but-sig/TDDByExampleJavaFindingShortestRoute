package com.sisbutsig.tddbyexample.shortestroute;

import java.util.Optional;

public final class Leg {
  private final String from;
  private final String to;
  private final double value;

  private Leg(String from, String to, double value) {
    this.from = from;
    this.to = to;
    this.value = value;
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public double getValue() {
    return value;
  }

  public static Leg of(String from, String to, double value) {
    if (value < 1) {
      throw new IllegalStateException("value is less than one.");
    }
    return new Leg(validate(from, "from is null or empty."),
        validate(to, "to is null or empty."), value);
  }

  private static String validate(String from, String expMsg) {
    return Optional.ofNullable(from)
                   .map(String::trim)
                   .filter(str -> !str.isEmpty())
                   .map(String::toUpperCase)
                   .orElseThrow(() -> new NullPointerException(expMsg));
  }

  @Override
  public String toString() {
    return "Leg [from=" + from + ", to=" + to + ", value=" + value + "]";
  }
}
