package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class KeepAliveDeactivationHook {
  @MappedRelationship(keepAlive = true) abstract List<Target> getItems();
  Stream<String> itemsStream() { return Stream.empty(); }
  Target createTarget(String source) { return Target.create(source); }
  @OnDeactivate void onItemsDeactivate() {}
}
