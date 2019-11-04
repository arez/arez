package com.example.repository;

import javax.annotation.Nonnull;

interface Extension2
{
  default MultiExtensionRepositoryExample findByName2( @Nonnull final String name )
  {
    return self().findByQuery( f -> f.getName().equals( name ) );
  }

  MultiExtensionRepositoryExampleRepository self();
}
