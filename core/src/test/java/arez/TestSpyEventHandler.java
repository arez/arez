package arez;

import arez.spy.SerializableEvent;
import arez.spy.SpyEventHandler;
import arez.spy.SpyEventTestUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.testng.Assert.*;

@SuppressWarnings( "NonJREEmulationClassesInClientCode" )
public final class TestSpyEventHandler
  implements SpyEventHandler
{
  @Nonnull
  private final ArezContext _context;
  @Nonnull
  private final List<Object> _events = new ArrayList<>();
  /**
   * When using assertNextEvent this tracks the index that we are up to.
   */
  private int _currentAssertIndex;

  TestSpyEventHandler( @Nonnull final ArezContext context )
  {
    _context = Objects.requireNonNull( context );
  }

  @Nonnull
  public static TestSpyEventHandler subscribe()
  {
    return subscribe( Arez.context() );
  }

  @Nonnull
  static TestSpyEventHandler subscribe( @Nonnull final ArezContext context )
  {
    final TestSpyEventHandler handler = new TestSpyEventHandler( context );
    context.getSpy().addSpyEventHandler( handler );
    return handler;
  }

  public void unsubscribe()
  {
    _context.getSpy().removeSpyEventHandler( this );
  }

  @Override
  public void onSpyEvent( @Nonnull final Object event )
  {
    _events.add( event );
  }

  public void assertEventCount( final int count )
  {
    assertEquals( _events.size(), count, "Actual events:\n" + eventsDebug() );
  }

  @Nonnull
  private String eventsDebug()
  {
    final AtomicInteger counter = new AtomicInteger( 0 );
    return
      _events
        .stream()
        .map( this::convertEventToString )
        .map( s -> counter.getAndIncrement() + ": " + s )
        .collect( Collectors.joining( "\n" ) );
  }

  @Nonnull
  private String convertEventToString( @Nonnull final Object event )
  {
    return ( event instanceof SerializableEvent se ) ?
           SpyEventTestUtil.toJsonObject( se, false ).toString() :
           event.toString();
  }

  /**
   * Assert "next" Event is of specific type.
   * Increment the next counter.
   */
  public <T> void assertNextEvent( @Nonnull final Class<T> type )
  {
    assertEvent( type, null );
  }

  /**
   * Assert "next" Event is of specific type.
   * Increment the next counter, run action.
   */
  public <T> void assertNextEvent( @Nonnull final Class<T> type, @Nonnull final Consumer<T> action )
  {
    assertEvent( type, action );
  }

  public void reset()
  {
    _events.clear();
    _currentAssertIndex = 0;
  }

  private <T> void assertEvent( @Nonnull final Class<T> type, @Nullable final Consumer<T> action )
  {
    assertTrue( _events.size() > _currentAssertIndex );
    final Object e = _events.get( _currentAssertIndex );
    assertTrue( type.isInstance( e ),
                "Expected event at index " + _currentAssertIndex + " to be of type " + type + " but is " +
                "of type " + e.getClass() + " with value " + e + "\n\n" +
                "Actual events:\n" + eventsDebug() );
    _currentAssertIndex++;
    final T event = type.cast( e );
    if ( null != action )
    {
      action.accept( event );
    }
  }
}
