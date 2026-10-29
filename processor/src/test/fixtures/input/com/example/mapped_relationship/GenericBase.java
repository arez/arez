package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponentLike
abstract class GenericBase<S extends @Nullable Object> {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE)
  abstract List<Target> getItems();
  protected Stream<S> itemsStream() { return Stream.empty(); }
  protected Target createTarget(final S source) { return Target.create(source); }
}
