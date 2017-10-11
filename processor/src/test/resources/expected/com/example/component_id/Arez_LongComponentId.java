package com.example.component_id;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_LongComponentId extends LongComponentId implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public Arez_LongComponentId() {
    super();
    this.$$arez$$_context = Arez.context();
  }

  String $$arez$$_name() {
    return "LongComponentId." + getId();
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
    return Long.hashCode( getId() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_LongComponentId) ) {
      return false;
    } else {
      final Arez_LongComponentId that = (Arez_LongComponentId) o;;
      return getId() == that.getId();
    }
  }
}
