package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedFactoryResult {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<GenericTarget<List<String>>> getItems();
  Stream<String> itemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "items") GenericTarget<List<@Nullable String>> factory(String source) { return GenericTarget.create(List.<@Nullable String>of()); }
}
