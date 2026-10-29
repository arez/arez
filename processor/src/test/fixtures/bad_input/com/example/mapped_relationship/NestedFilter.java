package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedFilter {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<GenericTarget<List<@Nullable String>>> getItems();
  Stream<String> itemsStream() { return Stream.empty(); }
  GenericTarget<List<@Nullable String>> createGenericTarget(String source) { return GenericTarget.create(List.<@Nullable String>of()); }
  @MappedRelationshipFilter(name = "items") boolean filter(GenericTarget<List<String>> target) { return true; }
}
