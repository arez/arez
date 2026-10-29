package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class UnknownComparator {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE)
  abstract List<Target> getItems();
  Stream<@Nullable String> itemsStream() { return Stream.empty(); }
  Target createTarget(final @Nullable String source) { return Target.create(source); }
  @MappedRelationshipComparator(name="missing") java.util.Comparator<Target> sorting() { return (a,b) -> 0; }
}
