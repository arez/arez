package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
@SuppressWarnings("rawtypes")
abstract class NestedRawFactoryInput {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getItems();
  Stream<List> itemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "items") Target factory(List<String> source) { return Target.create(source); }
}
