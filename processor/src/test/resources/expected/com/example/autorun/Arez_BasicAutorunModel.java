package com.example.autorun;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observer;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_BasicAutorunModel extends BasicAutorunModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observer $$arez$$_doStuff;

  public Arez_BasicAutorunModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_doStuff = this.$$arez$$_context.autorun( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".doStuff" : null, true, () -> super.doStuff(), false );
    this.$$arez$$_context.triggerScheduler();
  }

  final long $$arez$$_id() {
    return $$arez$$_id;
  }

  String $$arez$$_name() {
    return "BasicAutorunModel." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_doStuff.dispose();
    }
  }

  @Override
  public void doStuff() {
    assert !$$arez$$_disposed;
    this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".doStuff" : null, true, () -> super.doStuff() );
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arez$$_id() );
  }
}
