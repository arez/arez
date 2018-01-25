package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Computed;
import arez.annotations.Observable;
import arez.annotations.Repository;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Repository( extensions = { CompleteRepositoryExample.FooEx.class } )
@ArezComponent
public abstract class CompleteRepositoryExample
{
  // Unfortunately this this can not have nested content otherwise it will cause the
  // IDE to fail to compile as we put it on the path.
  public interface FooEx
  {
    //default Foo findByName( @Nonnull final String name )
    //{
    //  return self().findByQuery( f -> f.getName().equals( name ) );
    //}
    //
    //CompleteRepositoryExampleRepository self();
  }

  private final int _id;
  @Nonnull
  private String _name;
  @Nonnull
  private String _packageName;
  @Nullable
  private String _qualifiedName;

  CompleteRepositoryExample( @Nonnull final String packageName, @Nonnull final String name )
  {
    _id = 22;
    _packageName = packageName;
    _name = name;
  }

  @ComponentId
  final int getId()
  {
    return _id;
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

  @Observable
  @Nonnull
  public String getPackageName()
  {
    return _packageName;
  }

  public void setPackageName( @Nonnull final String packageName )
  {
    _packageName = packageName;
  }

  @Computed
  @Nonnull
  public String getQualifiedName()
  {
    final String rawQualifiedName = getRawQualifiedName();
    if ( null == rawQualifiedName )
    {
      return getPackageName() + "." + getName();
    }
    else
    {
      return rawQualifiedName;
    }
  }

  @Nullable
  @Observable
  public String getRawQualifiedName()
  {
    return _qualifiedName;
  }

  @Observable( name = "rawQualifiedName" )
  public void setQualifiedName( @Nullable final String qualifiedName )
  {
    _qualifiedName = qualifiedName;
  }
}
