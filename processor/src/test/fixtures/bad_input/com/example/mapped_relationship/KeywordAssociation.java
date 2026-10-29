package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class KeywordAssociation {
  @MappedRelationship abstract List<Target> getItems();
  @MappedRelationshipSource(name = "class") Stream<String> source() { return Stream.empty(); }
}
