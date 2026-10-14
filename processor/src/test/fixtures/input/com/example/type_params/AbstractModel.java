package com.example.type_params;

import arez.annotations.Action;
import arez.annotations.ComponentId;
import arez.annotations.Memoize;
import java.io.Writer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
abstract class AbstractModel<T extends Throwable, W extends Writer>
{
  public AbstractModel( @Nullable W writer )
  {
  }

  public AbstractModel( @Nullable T error )
  {
  }

  public AbstractModel( @Nullable T error, @Nullable W writer, int i )
  {
  }

  @Action
  public void handleWriter( @Nullable W writer )
  {
  }

  @Action
  public void handleError( @Nullable T error )
  {
  }

  @Action
  public @Nullable W genWriter()
  {
    return null;
  }

  @Action
  public @Nullable T genError()
  {
    return null;
  }

  @Memoize
  public @Nullable W compWriter()
  {
    return null;
  }

  @Memoize
  public @Nullable T compError()
  {
    return null;
  }

  @ComponentId
  public final T getComponentId()
  {
    throw new UnsupportedOperationException("Fixture declaration");
  }
}
