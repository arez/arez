package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ParametricInvariantFactory<S extends @Nullable Object> {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getItems();
  Stream<List<@NonNull S>> itemsStream() { return Stream.empty(); }
  Target createTarget(List<S> source) { return Target.create(source); }
}
