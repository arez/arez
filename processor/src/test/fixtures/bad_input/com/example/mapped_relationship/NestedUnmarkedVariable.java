package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedUnmarkedVariable extends UnmarkedVariableBase<@NonNull String> {}

@NullUnmarked
@ArezComponentLike
abstract class UnmarkedVariableBase<S> {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract @NonNull List<@NonNull Target> getItems();
  @NonNull Stream<S> itemsStream() { return Stream.empty(); }
}
