package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Repository;

@Repository( extensions = { RepositoryExtensionNotInterface.Foo.class } )
@ArezComponent
public class RepositoryExtensionNotInterface
{
  public enum Foo
  {
  }

  @Action
  public void doStuff()
  {
  }
}
