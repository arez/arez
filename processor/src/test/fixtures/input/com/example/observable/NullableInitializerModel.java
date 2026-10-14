package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import java.util.Date;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NullableInitializerModel
{
  @Observable( initializer = Feature.ENABLE )
  @Nullable
  public abstract Date getTime();

  public abstract void setTime( @Nullable Date value );
}
