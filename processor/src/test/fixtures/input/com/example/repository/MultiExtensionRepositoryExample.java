package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Repository;
import javax.annotation.Nonnull;

@Repository( extensions = { Extension1.class, Extension2.class } )
@ArezComponent
abstract class MultiExtensionRepositoryExample
{
  @Observable
  @Nonnull
  public abstract String getName();

  public abstract void setName( @Nonnull String name );
}
