package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class OverloadedModel {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE)
  abstract List<Target> getItems();
  Stream<String> itemsStream() { return Stream.empty(); }
  @MappedRelationship(depType = DepType.AREZ_OR_NONE)
  abstract List<Target> getNullItems();
  Stream<@Nullable String> nullItemsStream() { return Stream.empty(); }
  Target createTarget(final @Nullable Object source) { return Target.create(source); }
  String createTarget(final @Nullable String source) { return String.valueOf(source); }
  boolean filterTarget(final @Nullable Object target) { return true; }
  String filterTarget(final Target target) { return "unrelated return type"; }
  boolean retainTarget(final @Nullable Object target) { return false; }
  String retainTarget(final Target target) { return "unrelated return type"; }
}
