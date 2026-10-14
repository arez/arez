package com.example.on_deps_change;

import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface OnDepsChangeInterface
{
  @OnDepsChange
  void onRenderDepsChange();
}
