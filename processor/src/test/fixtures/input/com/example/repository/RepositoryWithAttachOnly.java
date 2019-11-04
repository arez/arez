package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Repository;
import javax.annotation.Nonnull;

@Repository( attach = Repository.AttachType.ATTACH_ONLY )
@ArezComponent
public abstract class RepositoryWithAttachOnly
{
  @Nonnull
  private String _name;

  RepositoryWithAttachOnly( @Nonnull final String name )
  {
    _name = name;
  }

  @Observable
  @Nonnull
  public String getName()
  {
    return _name;
  }

  public void setName( @Nonnull final String name )
  {
    _name = name;
  }
}
