package com.example.type_params;

import arez.annotations.Action;
import arez.annotations.ComponentId;
import arez.annotations.Computed;
import java.io.Writer;

public abstract class AbstractModel<T extends Throwable, W extends Writer>
{
  public AbstractModel( W writer )
  {
  }

  public AbstractModel( T error )
  {
  }

  public AbstractModel( T error, W writer, int i )
  {
  }

  @Action
  public void handleWriter( W writer )
  {
  }

  @Action
  public void handleError( T error )
  {
  }

  @Action
  public W genWriter()
  {
    return null;
  }

  @Action
  public T genError()
  {
    return null;
  }

  @Computed
  public W compWriter()
  {
    return null;
  }

  @Computed
  public T compError()
  {
    return null;
  }

  @ComponentId
  public final T getComponentId()
  {
    return null;
  }
}
