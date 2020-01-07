package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Repository;
import javax.annotation.Nonnull;

@Repository
@ArezComponent( defaultReadOutsideTransaction = Feature.ENABLE )
public abstract class ReadOutsideTransactionWithRepositoryModel
{
  @Observable
  @Nonnull
  public abstract String getName();

  public abstract void setName( @Nonnull String name );
}
