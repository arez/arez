package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedRawComparator {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getItems();
  Stream<String> itemsStream() { return Stream.empty(); }
  Target createTarget(String source) { return Target.create(source); }
  @MappedRelationshipComparator(name = "items") Raw comparator() { return new Raw(); }
  @SuppressWarnings("rawtypes") static class Raw implements Comparator {
    public int compare(Object a, Object b) { return 0; }
  }
}
