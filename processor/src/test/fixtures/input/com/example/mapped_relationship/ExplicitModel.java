package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ExplicitModel {
  @MappedRelationship(name = "mapped", keepAlive = true, priority = Priority.LOW,
      observeLowerPriorityDependencies = true, depType = DepType.AREZ_OR_EXTERNAL,
      reportResult = false, readOutsideTransaction = Feature.ENABLE,
      writeOutsideTransaction = Feature.ENABLE, equalityComparator = AlwaysEqual.class)
  abstract List<Target> getItems();
  @MappedRelationshipSource(name = "mapped")
  Stream<@Nullable String> sources() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "mapped")
  Target create(final @Nullable String source) { return Target.create(source); }
  @ComputableValueRef(name = "mapped")
  abstract ComputableValue<List<Target>> reference();
  public static final class AlwaysEqual implements EqualityComparator {
    public boolean areEqual(final @Nullable Object a, final @Nullable Object b) { return true; }
  }
}
