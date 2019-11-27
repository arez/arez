package com.example.on_deps_change;

import arez.annotations.OnDepsChange;

interface OnDepsChangeInterface
{
  @OnDepsChange
  void onRenderDepsChange();
}
