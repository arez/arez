package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ParametricSource<S extends @Nullable Object> {
  @MappedRelationship abstract List<Target> getItems();
  Stream<S> itemsStream() { return Stream.empty(); }
}
