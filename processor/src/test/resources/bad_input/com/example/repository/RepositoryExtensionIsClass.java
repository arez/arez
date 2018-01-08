package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Repository;

@Repository( extensions = { RepositoryExtensionIsClass.Foo.class } )
@ArezComponent
public class RepositoryExtensionIsClass
{
  public static class Foo
  {
  }

  @Action
  public void doStuff()
  {
  }
}
