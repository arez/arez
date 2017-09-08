package org.realityforge.arez.extras;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Observer;
import org.realityforge.arez.Procedure;
import org.realityforge.arez.SafeFunction;

/**
 * This class is used to watch state and when a condition is true, then run effect and remove watch.
 *
 * <p>This is a good example of how the primitives provided by Arez can be glued together
 * to create higher level reactive elements.</p>
 */
public final class Watcher
{
  @Nonnull
  private final ComputedValue<Boolean> _conditionValue;
  private final Observer _observer;

  public Watcher( @Nonnull final ArezContext context,
                  @Nullable final String name,
                  final boolean mutation,
                  @Nonnull final SafeFunction<Boolean> condition,
                  @Nonnull final Procedure action )
  {
    _conditionValue = context.createComputedValue( name, condition, Objects::equals );
    final Procedure procedure = () -> {
      if ( Boolean.TRUE == _conditionValue.get() )
      {
        context.procedure( name, mutation, action );
        dispose();
      }
    };
    _observer = context.autorun( name, mutation, procedure, true );
  }

  public void dispose()
  {
    _conditionValue.dispose();
    _observer.dispose();
  }
}
