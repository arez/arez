package com.example.collections;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ArezComponent
public abstract class ObservableNoSettersModel
{
  @Observable( expectSetter = false )
  public Collection<String> getCollection()
  {
    return new HashSet<>();
  }

  @ObservableValueRef
  protected abstract ObservableValue getCollectionObservableValue();

  @Observable( expectSetter = false )
  public Set<String> getSet()
  {
    return new HashSet<>();
  }

  @ObservableValueRef
  protected abstract ObservableValue getSetObservableValue();

  @Observable( expectSetter = false )
  public List<String> getList()
  {
    return new ArrayList<>();
  }

  @ObservableValueRef
  protected abstract ObservableValue getListObservableValue();

  @Observable( expectSetter = false )
  public Map<String, String> getMap()
  {
    return new HashMap<>();
  }

  @ObservableValueRef
  protected abstract ObservableValue getMapObservableValue();
}
