package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Repository;

@Repository
@ArezComponent( observable = Feature.DISABLE )
public abstract class RepositoryNotObservable
{
  public enum Foo
  {
  }

  @Action
  public void doStuff()
  {
  }
}
