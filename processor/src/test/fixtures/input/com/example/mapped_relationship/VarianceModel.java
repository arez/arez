package com.example.mapped_relationship;

import arez.annotations.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class VarianceModel<S extends Serializable & List<@Nullable String>> {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getCollections();
  Stream<ArrayList<@Nullable String>> collectionsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "collections") Target collection(Collection<@Nullable String> source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getExtendsItems();
  Stream<List<? extends @Nullable String>> extendsItemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "extendsItems") Target extendsItem(List<? extends @Nullable String> source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getSuperItems();
  Stream<List<@Nullable String>> superItemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "superItems") Target superItem(List<? super String> source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getUnknownItems();
  Stream<List<?>> unknownItemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "unknownItems") Target unknownItem(List<? extends @Nullable Object> source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getAnyItems();
  Stream<List<String>> anyItemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "anyItems") Target anyItem(List<?> source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getArrays();
  Stream<@Nullable String[]> arraysStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "arrays") Target array(@Nullable String[] source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getArrayObjects();
  Stream<String[]> arrayObjectsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "arrayObjects") Target arrayObject(Object source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getBounds();
  Stream<S> boundsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "bounds") Target bound(List<@Nullable String> source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getRecursive();
  Stream<Self> recursiveStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "recursive") Target recursive(Comparable<Self> source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getNested();
  Stream<Outer<@Nullable String>.Inner<@Nullable String>> nestedStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "nested") Target nested(Outer<@Nullable String>.Inner<@Nullable String> source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getSameBounds();
  Stream<S> sameBoundsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "sameBounds") Target sameBound(S source) { return Target.create(source); }

  static class Self implements Comparable<Self> {
    public int compareTo(Self other) { return 0; }
  }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getNestedSuper();
  Stream<Outer<@Nullable String>.Inner<String>> nestedSuperStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "nestedSuper") Target nestedSuper(Box<@Nullable String> source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getNullableVariables();
  Stream<@Nullable S> nullableVariablesStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "nullableVariables") Target nullableVariable(@Nullable S source) { return Target.create(source); }

  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<Target> getNonNullVariables();
  Stream<List<@NonNull S>> nonNullVariablesStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "nonNullVariables") Target nonNullVariable(List<@NonNull S> source) { return Target.create(source); }

  interface Box<V extends @Nullable Object> {}
  static class Outer<T extends @Nullable Object> {
    class Inner<U extends @Nullable Object> implements Box<T> {}
  }
}
