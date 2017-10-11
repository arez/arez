package com.example.component_id;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_ShortComponentId extends ShortComponentId implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public Arez_ShortComponentId() {
    super();
    this.$$arez$$_context = Arez.context();
  }

  String $$arez$$_name() {
    return "ShortComponentId." + getId();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
    }
  }

  @Override
  public final int hashCode() {
    return Short.hashCode( getId() );
  }
}
