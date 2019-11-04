package com.example.repository;

import javax.annotation.Nonnull;

interface Extension1
{
  default MultiExtensionRepositoryExample findByName( @Nonnull final String name )
  {
    return self().findByQuery( f -> f.getName().equals( name ) );
  }

  MultiExtensionRepositoryExampleRepository self();
}
