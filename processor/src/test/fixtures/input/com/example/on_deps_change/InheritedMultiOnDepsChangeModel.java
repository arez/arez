package com.example.on_deps_change;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChange;
import com.example.on_deps_change.other.BaseInheritedMultiOnDepsChangeModel;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class InheritedMultiOnDepsChangeModel
  extends BaseInheritedMultiOnDepsChangeModel
{
  @OnDepsChange( name = "render" )
  void onRenderDepsChange2( @NonNull final Observer observer )
  {
  }
}
