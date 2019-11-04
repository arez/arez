package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Repository;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;

@Repository
@ArezComponent
@SuppressWarnings( "rawtypes" )
public abstract class RepositoryWithRawType
{
  @Observable
  @Nonnull
  public abstract Callable getAction();

  public abstract void setAction( @Nonnull Callable action );
}
