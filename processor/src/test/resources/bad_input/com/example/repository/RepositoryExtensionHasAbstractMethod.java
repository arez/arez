package com.example.repository;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Repository;

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
