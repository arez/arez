package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.function.Supplier;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponentLike
abstract class ContainerBase<L, S> {
  private final Supplier<S> _sources;
  ContainerBase(final Supplier<S> sources) { _sources = sources; }
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract L getItems();
  S itemsStream() { return _sources.get(); }
  Target createTarget(String source) { return Target.create(source); }
}
