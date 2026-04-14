package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when Transaction starts.
 */
public final class TransactionStartEvent
  implements SerializableEvent
{
  @Nonnull
  private final String _name;
  private final boolean _mutation;
  @Nullable
  private final ObserverInfo _tracker;

  public TransactionStartEvent( @Nonnull final String name,
                                final boolean mutation,
                                @Nullable final ObserverInfo tracker )
  {
    _name = Objects.requireNonNull( name );
    _mutation = mutation;
    _tracker = tracker;
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
  public ObserverInfo getTracker()
  {
    return _tracker;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "TransactionStart" );
    map.put( "name", getName() );
    map.put( "mutation", isMutation() );
    final ObserverInfo tracker = getTracker();
    map.put( "tracker", null == tracker ? null : tracker.getName() );
    SpyEventUtil.maybeAddZone( map );
  }
}
