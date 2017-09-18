package com.example.computed;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_ComputedWithHooksModel extends ComputedWithHooksModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final ComputedValue<Long> $$arez$$_time;

  public Arez_ComputedWithHooksModel(@Nonnull final ArezContext $$arez$$_context) {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null, super::getTime, Objects::equals, this::onTimeActivate, this::onTimeDeactivate, this::onTimeStale );
  }

  private String $$arez$$_id() {
    return "ComputedWithHooksModel." + $$arez$$_id + ".";
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_time.dispose();
    }
  }

  @Override
  public long getTime() {
    return this.$$arez$$_time.get();
  }
}
