package org.realityforge.arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Observer;

/**
 * Notification when Transaction completes.
 */
public final class TransactionCompletedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( TransactionCompletedEvent.class );

  @Nonnull
  private final String _name;
  private final boolean _mutation;
  @Nullable
  private final Observer _tracker;
  @Nonnegative
  private final long _duration;

  public TransactionCompletedEvent( @Nonnull final String name,
                                    final boolean mutation,
                                    @Nullable final Observer tracker,
                                    @Nonnegative final long duration )
  {
    assert duration >= 0;
    _name = Objects.requireNonNull( name );
    _mutation = mutation;
    _tracker = tracker;
    _duration = duration;
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }

  public boolean isMutation()
  {
    return _mutation;
  }

  @Nullable
  public Observer getTracker()
  {
    return _tracker;
  }

  @Nonnegative
  public long getDuration()
  {
    return _duration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", TYPE_NAME );
    map.put( "transaction", getName() );
    map.put( "mutation", isMutation() );
    final Observer tracker = getTracker();
    map.put( "tracker", null == tracker ? null : tracker.getName() );
    map.put( "duration", getDuration() );
  }
}
