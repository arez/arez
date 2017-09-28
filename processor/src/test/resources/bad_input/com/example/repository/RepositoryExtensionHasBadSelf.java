package com.example.repository;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Repository;

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
