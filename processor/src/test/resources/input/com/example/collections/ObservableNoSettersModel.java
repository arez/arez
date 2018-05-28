package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
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

  @ObservableRef
  protected abstract arez.Observable getCollectionObservable();

  @Observable( expectSetter = false )
  public Set<String> getSet()
  {
    return new HashSet<>();
  }

  @ObservableRef
  protected abstract arez.Observable getSetObservable();

  @Observable( expectSetter = false )
  public List<String> getList()
  {
    return new ArrayList<>();
  }

  @ObservableRef
  protected abstract arez.Observable getListObservable();

  @Observable( expectSetter = false )
  public Map<String, String> getMap()
  {
    return new HashMap<>();
  }

  @ObservableRef
  protected abstract arez.Observable getMapObservable();
}
