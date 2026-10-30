package com.example.mapped_relationship;

import arez.ComputableValue;
import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponentLike
interface ContextAccess<C extends @Nullable Object> {
  C context();
  @MemoizeContextParameter default C captureContext() { return context(); }
  default void pushContext(C value) {}
  default void popContext(C value) {}
}

@NullMarked
@ArezComponentLike
abstract class GenericContextBase<C extends @Nullable Object> implements ContextAccess<C> {
  private final C value;
  GenericContextBase(C value) { this.value = value; }
  @Override public C context() { return value; }
  @MappedRelationship(depType = DepType.AREZ_OR_EXTERNAL, useEqualsForKeys = true) abstract List<Target> getItems();
  @ComputableValueRef abstract ComputableValue<List<Target>> getItemsComputableValue();
  Stream<@Nullable String> itemsStream() { return Stream.of((String) null); }
  Target createTarget(@Nullable String source) { return Target.create(source); }
  @Memoize(depType = DepType.AREZ_OR_NONE) String getSummary() { return "summary"; }
  @ComputableValueRef abstract ComputableValue<String> getSummaryComputableValue();
}
