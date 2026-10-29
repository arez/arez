package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class GenericFactory {
  @MappedRelationship abstract List<Target> getItems();
  Stream<String> itemsStream() { return Stream.empty(); }
  @MappedRelationshipFactory(name = "items") <T> Target factory(String source) { return Target.create(source); }
}
