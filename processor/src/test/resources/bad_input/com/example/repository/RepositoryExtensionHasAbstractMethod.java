package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Repository;

@Repository( extensions = { RepositoryExtensionHasAbstractMethod.Foo.class } )
@ArezComponent
public class RepositoryExtensionHasAbstractMethod
{
  public interface Foo
  {
    void other( int i );
  }

  @Action
  public void doStuff()
  {
  }
}
