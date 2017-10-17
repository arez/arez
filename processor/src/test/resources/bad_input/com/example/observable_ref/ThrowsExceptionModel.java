package com.example.observable_ref;

import java.text.ParseException;
import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.ObservableRef;

@ArezComponent
public class ThrowsExceptionModel
{
  @Observable
  public long getTime()
  {
    return 0;
  }

  public void setTime( final long time )
  {
  }

  @Nonnull
  @ObservableRef
  public org.realityforge.arez.Observable getTimeObservable()
    throws ParseException
  {
    throw new IllegalStateException();
  }
}
