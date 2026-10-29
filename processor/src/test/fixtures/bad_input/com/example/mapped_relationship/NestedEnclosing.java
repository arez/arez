package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedEnclosing {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getItems();
  Stream<Outer<@Nullable String>.Inner<String>> itemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "items") Target factory(Outer<String>.Inner<String> source) { return Target.create(source); }
  static class Outer<T extends @Nullable Object> { class Inner<U extends @Nullable Object> {} }
}
