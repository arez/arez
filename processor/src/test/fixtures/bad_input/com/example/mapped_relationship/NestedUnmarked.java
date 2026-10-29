package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullUnmarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedUnmarked {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract @NonNull List<@NonNull Target> getItems();
  @NonNull Stream<@NonNull List<@NonNull String>> itemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "items") @NonNull Target factory(@NonNull List<String> source) { return Target.create(source); }
}
