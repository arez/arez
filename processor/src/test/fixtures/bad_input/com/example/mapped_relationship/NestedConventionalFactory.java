package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedConventionalFactory {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getItems();
  Stream<List<@Nullable String>> itemsStream() { return Stream.empty(); }
  Target createTarget(List<String> source) { return Target.create(source); }
}
