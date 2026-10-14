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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableNoSettersModel
{
  @Observable( expectSetter = false )
  public @Nullable Collection<String> getCollection()
  {
    return new HashSet<>();
  }

  @ObservableValueRef
  abstract @Nullable ObservableValue<Collection<String>> getCollectionObservableValue();

  @Observable( expectSetter = false )
  public @Nullable Set<String> getSet()
  {
    return new HashSet<>();
  }

  @ObservableValueRef
  abstract @Nullable ObservableValue<Set<String>> getSetObservableValue();

  @Observable( expectSetter = false )
  public @Nullable List<String> getList()
  {
    return new ArrayList<>();
  }

  @ObservableValueRef
  abstract @Nullable ObservableValue<List<String>> getListObservableValue();

  @Observable( expectSetter = false )
  public @Nullable Map<String, String> getMap()
  {
    return new HashMap<>();
  }

  @ObservableValueRef
  abstract @Nullable ObservableValue<Map<String, String>> getMapObservableValue();
}
