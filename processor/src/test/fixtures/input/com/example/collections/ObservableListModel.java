package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableListModel
{
  @Observable
  public @Nullable List<String> getMyValue()
  {
    return null;
  }

  public void setMyValue( final @Nullable List<String> value )
  {
  }
}
