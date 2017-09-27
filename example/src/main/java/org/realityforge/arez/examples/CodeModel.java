package org.realityforge.arez.examples;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Observable;

@SuppressWarnings( "WeakerAccess" )
@ArezComponent
public class CodeModel
{
  @Nonnull
  private String _name;
  @Nonnull
  private String _packageName;
  @Nullable
  private String _qualifiedName;

  @Nonnull
  public static CodeModel create( @Nonnull final String packageName, @Nonnull final String name )
  {
    return new Arez_CodeModel( packageName, name );
  }

  CodeModel( @Nonnull final String packageName, @Nonnull final String name )
  {
    _packageName = packageName;
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
