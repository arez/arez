package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponentLike
abstract class GenericTargetBase<T extends Target> {
  private final Function<@Nullable String, T> _factory;
  GenericTargetBase(final Function<@Nullable String, T> factory) { _factory = factory; }
  @MappedRelationship(depType = DepType.AREZ_OR_NONE)
  abstract List<T> getItems();
  Stream<@Nullable String> itemsStream() { return Stream.empty(); }
  T createTarget(final @Nullable String source) { return _factory.apply(source); }
  @OnActivate void onItemsActivate(ComputableValue<List<T>> value) {}
}
