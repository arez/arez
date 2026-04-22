package com.example.on_deps_change;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChange;
import com.example.on_deps_change.other.BaseInheritedMultiOnDepsChangeModel;
import javax.annotation.Nonnull;

@ArezComponent
abstract class InheritedMultiOnDepsChangeModel
  extends BaseInheritedMultiOnDepsChangeModel
{
  @OnDepsChange( name = "render" )
  void onRenderDepsChange2( @Nonnull final Observer observer )
  {
  }
}
