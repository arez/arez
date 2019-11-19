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
abstract class ObservableNoSettersModel
{
  @Observable( expectSetter = false )
  public Collection<String> getCollection()
  {
    return new HashSet<>();
  }

  @ObservableValueRef
  abstract ObservableValue<Collection<String>> getCollectionObservableValue();

  @Observable( expectSetter = false )
  public Set<String> getSet()
  {
    return new HashSet<>();
  }

  @ObservableValueRef
  abstract ObservableValue<Set<String>> getSetObservableValue();

  @Observable( expectSetter = false )
  public List<String> getList()
  {
    return new ArrayList<>();
  }

  @ObservableValueRef
  abstract ObservableValue<List<String>> getListObservableValue();

  @Observable( expectSetter = false )
  public Map<String, String> getMap()
  {
    return new HashMap<>();
  }

  @ObservableValueRef
  abstract ObservableValue<Map<String, String>> getMapObservableValue();
}
