package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ContextActivate {
  @MappedRelationship abstract List<Target> getItems();
  Stream<String> itemsStream() { return Stream.empty(); }
  Target createTarget(String source) { return Target.create(source); }
  @MemoizeContextParameter String captureContext() { return "context"; }
  void pushContext(String value) {}
  void popContext(String value) {}
  @OnActivate void onItemsActivate() {}
}
