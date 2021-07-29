package arez.spytools;

import arez.Arez;
import arez.spy.SpyEventHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * Abstract base class for processing spy events.
 * Simplifies handling of events by delegating to a specific processor
 * based on types of the events. Note that the type must be the concrete
 * type of the subclass.
 */
public abstract class AbstractSpyEventProcessor
  implements SpyEventHandler
{
  /**
   * The processors that can be delegated to.
   */
  private final Map<Class<?>, BiConsumer<SpyUtil.NestingDelta, ?>> _processors = new HashMap<>();
  /**
   * The current nesting level.
   */
  private int _nestingLevel;

  /**
   * Method invoked by subclasses to register
   *
   * @param <T>       the event type.
   * @param type      the type of the event to register.
   * @param processor the processor to handle event with.
   */
  protected final <T> void on( @Nonnull final Class<T> type,
                               @Nonnull final BiConsumer<SpyUtil.NestingDelta, T> processor )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_processors.containsKey( type ),
                    () -> "Attempting to call AbstractSpyEventProcessor.on() to register a processor " +
                          "for type " + type + " but an existing processor already exists for type" );
    }
    _processors.put( type, processor );
  }

  /**
   * Handle the specified event by delegating to the registered processor.
   *
   * @param event the event that occurred.
   */
  @Override
  @SuppressWarnings( { "ConstantConditions", "unchecked" } )
  public final void onSpyEvent( @Nonnull final Object event )
  {
    assert null != event;
    final BiConsumer<SpyUtil.NestingDelta, Object> processor =
      (BiConsumer<SpyUtil.NestingDelta, Object>) _processors.get( event.getClass() );
    if ( null != processor )
    {
      final SpyUtil.NestingDelta delta = getNestingDelta( event );
      if ( SpyUtil.NestingDelta.DECREASE == delta )
      {
        _nestingLevel -= 1;
        decreaseNestingLevel();
      }
      processor.accept( delta, event );
      if ( SpyUtil.NestingDelta.INCREASE == delta )
      {
        _nestingLevel += 1;
        increaseNestingLevel();
      }
    }
    else
    {
      handleUnhandledEvent( event );
    }
  }

  /**
   * Return the current nesting level.
   *
   * @return the current nesting level.
   */
  protected final int getNestingLevel()
  {
    return _nestingLevel;
  }

  /**
   * Hook method called when the nesting level increases.
   * Override as appropriate in subclasses.
   */
  protected void increaseNestingLevel()
  {
  }

  /**
   * Hook method called when the nesting level decreases.
   * Override as appropriate in subclasses.
   */
  protected void decreaseNestingLevel()
  {
  }

  /**
   * Handle the specified event that had no processors defined for it.
   *
   * @param event the unhandled event.
   */
  protected void handleUnhandledEvent( @Nonnull final Object event )
  {
  }

  /**
   * Return the change in nesting level for event if any.
   * This method is used rather than directly deferring to {@link SpyUtil#getNestingDelta(Class)} so that
   * subclasses can handle custom events.
   *
   * @param event the event.
   * @return the delta in nesting level.
   */
  @Nonnull
  protected SpyUtil.NestingDelta getNestingDelta( @Nonnull final Object event )
  {
    return SpyUtil.getNestingDelta( event.getClass() );
  }
}
