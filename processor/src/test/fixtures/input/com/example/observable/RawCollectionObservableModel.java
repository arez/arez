package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.List;

@SuppressWarnings( "rawtypes" )
@ArezComponent
abstract class RawCollectionObservableModel
{
  @Observable
  public abstract List getMyValue();

  public abstract void setMyValue( final List value );
}
