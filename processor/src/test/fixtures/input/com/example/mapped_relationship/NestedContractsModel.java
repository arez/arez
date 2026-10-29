package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.*;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedContractsModel {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE) abstract List<GenericTarget<List<@Nullable String>>> getItems();
  Stream<List<@Nullable String>> itemsStream() { return Stream.empty(); }
  GenericTarget<List<@Nullable String>> createGenericTarget(Object source) { return GenericTarget.create(Arrays.asList((String) null)); }
  GenericTarget<List<@Nullable String>> createGenericTarget(List<String> source) { return GenericTarget.create(source); }
  boolean filterGenericTarget(Object target) { return true; }
  boolean filterGenericTarget(GenericTarget<List<String>> target) { return true; }
  boolean retainGenericTarget(Object target) { return false; }
  boolean retainGenericTarget(GenericTarget<List<String>> target) { return false; }
  Comparator<? super GenericTarget<List<@Nullable String>>> itemsComparator() { return (a,b) -> 0; }
}
