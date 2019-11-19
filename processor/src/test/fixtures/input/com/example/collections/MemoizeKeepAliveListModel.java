package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.ArrayList;
import java.util.List;

@ArezComponent
abstract class MemoizeKeepAliveListModel
{
  @Memoize( keepAlive = true )
  public List<String> getMyValue()
  {
    return new ArrayList<>();
  }
}
