package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ParametricInputModel<S extends @Nullable Object> {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getItems();
  Stream<@NonNull S> itemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "items") Target createTarget(S source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getProducers();
  Stream<List<@NonNull S>> producersStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "producers") Target producer(List<? extends S> source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getConsumers();
  Stream<List<S>> consumersStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "consumers") Target consumer(List<? super @NonNull S> source) { return Target.create(source); }
}
