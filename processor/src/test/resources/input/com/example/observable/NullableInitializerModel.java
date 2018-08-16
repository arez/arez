package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import java.util.Date;
import javax.annotation.Nullable;

@ArezComponent
public abstract class NullableInitializerModel
{
  @Observable( initializer = Feature.ENABLE )
  @Nullable
  public abstract Date getTime();

  public abstract void setTime( Date value );
}
