package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ConventionalModel {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE)
  abstract List<Target> getItems();
  Stream<@Nullable String> itemsStream() { return Stream.empty(); }
  Target createTarget(final @Nullable String source) { return Target.create(source); }
  String itemsStream(final int unrelated) { return ""; }
  Target createTarget(final int unrelated) { return Target.create(unrelated); }
  String filterTarget(final String unrelated) { return unrelated; }
  String itemsComparator() { return "not a comparator"; }
  Boolean retainTarget(final Target unrelated) { return Boolean.TRUE; }
}
