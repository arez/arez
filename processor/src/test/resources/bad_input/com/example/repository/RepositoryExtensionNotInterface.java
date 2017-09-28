package com.example.repository;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Repository;

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
