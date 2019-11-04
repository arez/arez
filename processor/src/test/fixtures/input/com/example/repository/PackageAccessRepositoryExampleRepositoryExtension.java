package com.example.repository;

import javax.annotation.Nonnull;

interface PackageAccessRepositoryExampleRepositoryExtension
{
  default PackageAccessRepositoryExample findByName( @Nonnull final String name )
  {
    return self().findByQuery( f -> f.getName().equals( name ) );
  }

  PackageAccessRepositoryExampleRepository self();
}
