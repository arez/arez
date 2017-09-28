package com.example.repository;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Repository;

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
