package com.example.reserved_names;

import arez.annotations.ArezComponent;

/**
 * These names are all valid as they have parameters and wont collide with built in Arez intrinsics.
 */
@ArezComponent( allowEmpty = true )
public abstract class NonReservedNameModel
{
  void dispose( int i )
  {
  }

  void isDisposed( int i )
  {
  }

  void getArezId( int i )
  {
  }

  void observe( int i )
  {
  }
}
