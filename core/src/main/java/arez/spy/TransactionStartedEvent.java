package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when Transaction starts.
 */
public final class TransactionStartedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( TransactionStartedEvent.class );
  @Nonnull
  private final String _name;
  private final boolean _mutation;
  @Nullable
  private final ObserverInfo _tracker;

  public TransactionStartedEvent( @Nonnull final String name,
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", TYPE_NAME );
    map.put( "transaction", getName() );
    map.put( "mutation", isMutation() );
    final ObserverInfo tracker = getTracker();
    map.put( "tracker", null == tracker ? null : tracker.getName() );
  }
}
