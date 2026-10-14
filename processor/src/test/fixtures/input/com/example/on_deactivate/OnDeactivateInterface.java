package com.example.on_deactivate;

import arez.annotations.OnDeactivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface OnDeactivateInterface
{
  @OnDeactivate
  void onTimeDeactivate();
}
