package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullUnmarked
@ArezComponent(sting = Feature.DISABLE)
abstract class UnspecifiedSourceElements {
  @MappedRelationship abstract @NonNull List<@NonNull Target> getItems();
  @NonNull Stream<String> itemsStream() { return Stream.empty(); }
  @NonNull Target createTarget(@Nullable String source) { return Target.create(source); }
}
