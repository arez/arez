package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedRetention {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<GenericTarget<List<@Nullable String>>> getItems();
  Stream<String> itemsStream() { return Stream.empty(); }
  GenericTarget<List<@Nullable String>> createGenericTarget(String source) { return GenericTarget.create(List.<@Nullable String>of()); }
  @MappedRelationshipRetention(name = "items") boolean retain(GenericTarget<List<String>> target) { return true; }
}
