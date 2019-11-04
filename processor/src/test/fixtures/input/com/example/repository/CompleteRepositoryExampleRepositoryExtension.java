package com.example.repository;

import javax.annotation.Nonnull;

public interface CompleteRepositoryExampleRepositoryExtension
{
  default CompleteRepositoryExample findByName( @Nonnull final String name )
  {
    return self().findByQuery( f -> f.getName().equals( name ) );
  }

  CompleteRepositoryExampleRepository self();
}
