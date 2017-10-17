package com.example.action;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_FunctionActionThrowsThrowableModel extends FunctionActionThrowsThrowableModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public Arez_FunctionActionThrowsThrowableModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
  }

  final long $$arez$$_id() {
    return $$arez$$_id;
  }

  String $$arez$$_name() {
    return "FunctionActionThrowsThrowableModel." + $$arez$$_id();
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
  public int doStuff(final long time) throws Throwable {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    try {
      return this.$$arez$$_context.action(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".doStuff" : null, true, () -> super.doStuff(time), time );
    } catch( final Throwable $$arez$$_e ) {
      throw $$arez$$_e;
    }
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arez$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_FunctionActionThrowsThrowableModel) ) {
      return false;
    } else {
      final Arez_FunctionActionThrowsThrowableModel that = (Arez_FunctionActionThrowsThrowableModel) o;;
      return $$arez$$_id() == that.$$arez$$_id();
    }
  }

  @Override
  public final String toString() {
    if ( $$arez$$_context.areNamesEnabled() ) {
      return "ArezComponent[" + $$arez$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
