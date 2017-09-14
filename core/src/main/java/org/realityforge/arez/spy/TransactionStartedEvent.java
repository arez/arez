package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Node;

/**
 * Notification when Transaction starts.
 */
public final class TransactionStartedEvent
{
  @Nonnull
  private final String _name;
  private final boolean _mutation;
  @Nullable
  private final Node _tracker;

  public TransactionStartedEvent( @Nonnull final String name,
                                  final boolean mutation,
                                  @Nullable final Node tracker )
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
  public Node getTracker()
  {
    return _tracker;
  }
}
