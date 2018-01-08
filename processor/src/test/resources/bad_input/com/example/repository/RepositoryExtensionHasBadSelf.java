package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Repository;

@Repository( extensions = { RepositoryExtensionHasBadSelf.Foo.class } )
@ArezComponent
public class RepositoryExtensionHasBadSelf
{
  public interface Foo
  {
    void self( int i );

  }

  @Action
  public void doStuff()
  {
  }
}
