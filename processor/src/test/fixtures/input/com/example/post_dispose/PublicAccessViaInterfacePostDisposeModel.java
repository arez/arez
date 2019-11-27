package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent( allowEmpty = true )
abstract class PublicAccessViaInterfacePostDisposeModel
  implements PostDisposeInterface
{
  @Override
  @PostDispose
  public void postDispose()
  {
  }
}
