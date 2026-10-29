package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedInvariant {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getItems();
  Stream<List<String>> itemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "items") Target factory(List<@Nullable String> source) { return Target.create(source); }
}
