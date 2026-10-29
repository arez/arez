package com.example.mapped_relationship;

import arez.annotations.*;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ContainerModel extends ContainerBase<List<Target>, Stream<String>> implements Unrelated {
  ContainerModel() { super(Stream::empty); }
}

interface Unrelated {}
