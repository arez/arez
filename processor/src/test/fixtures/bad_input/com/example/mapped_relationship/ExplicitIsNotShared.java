package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ExplicitIsNotShared {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE)
  abstract List<Target> getItems();
  Stream<@Nullable String> itemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name="items") Target createTarget(final @Nullable String source) { return Target.create(source); }
  @MappedRelationship(depType=DepType.AREZ_OR_NONE) abstract List<Target> getOthers(); Stream<@Nullable String> othersStream() { return Stream.empty(); }
}
