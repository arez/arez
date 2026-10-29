package com.example.mapped_relationship;

import arez.*;
import arez.annotations.*;
import java.util.List;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class InheritedTargetModel extends GenericTargetBase<Target> {
  InheritedTargetModel() { super(Target::create); }
  @ComputableValueRef abstract ComputableValue<List<Target>> getItemsComputableValue();
}
