package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ObservableNonnullListModel
{
  @Nonnull
  @Observable
  public List<String> getMyValue()
  {
    return new ArrayList<>();
  }

  public void setMyValue( final List<String> value )
  {
  }
}
