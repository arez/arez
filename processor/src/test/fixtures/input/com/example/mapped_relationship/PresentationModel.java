package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class PresentationModel {
  @MappedRelationship(depType = DepType.AREZ_OR_NONE)
  abstract List<Target> getItems();
  Stream<@Nullable String> itemsStream() { return Stream.empty(); }
  Target createTarget(final @Nullable String source) { return Target.create(source); }
  boolean filterTarget(final Target target) { return null != target.source; }
  Comparator<Target> itemsComparator() { return Comparator.comparing(target -> String.valueOf(target.source)); }
  boolean retainTarget(final Target target) { return "saved".equals(target.source); }
}
