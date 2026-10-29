package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
@SuppressWarnings("rawtypes")
abstract class NestedRawFactoryResult {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<GenericTarget<List<String>>> getItems();
  Stream<String> itemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "items") GenericTarget factory(String source) { return GenericTarget.create(List.of()); }
}
