package com.example.post_dispose;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( disposeNotifier = Feature.DISABLE )
abstract class PostDisposeWithDisabledDisposeNotifierModel
{
  @PostDispose
  void postDispose()
  {
  }

  @Action
  public int someValue()
  {
    return 0;
  }
}
