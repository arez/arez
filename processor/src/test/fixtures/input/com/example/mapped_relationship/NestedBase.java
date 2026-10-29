package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponentLike
abstract class NestedBase<S extends @Nullable Object> {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getItems();
  Stream<List<S>> itemsStream() { return Stream.empty(); }
  Target createTarget(List<String> source) { return Target.create(source); }
  Target createTarget(Object source) { return Target.create(source); }
}
