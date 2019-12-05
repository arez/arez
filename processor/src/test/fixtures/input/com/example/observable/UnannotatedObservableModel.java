package com.example.observable;

import arez.annotations.ArezComponent;

@ArezComponent
abstract class UnannotatedObservableModel
{
  abstract long getTime();

  abstract void setTime( long value );

  // This should not be made observable as they are concrete
  int getX()
  {
    return 0;
  }

  void setX( int x )
  {
  }
}
