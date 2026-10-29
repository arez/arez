package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ConcreteGetter {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE)
  List<Target> getItems() { return List.of(); }
  Stream<@Nullable String> itemsStream() { return Stream.empty(); }
  Target createTarget(final @Nullable String source) { return Target.create(source); }
}
