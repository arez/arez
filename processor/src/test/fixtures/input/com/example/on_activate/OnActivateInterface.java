package com.example.on_activate;

import arez.annotations.OnActivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface OnActivateInterface
{
  @OnActivate
  void onTimeActivate();
}
