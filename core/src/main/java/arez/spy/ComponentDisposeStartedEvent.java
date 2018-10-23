package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when a Component is disposed.
 */
public final class ComponentDisposeStartedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComponentInfo _componentInfo;

  public ComponentDisposeStartedEvent( @Nonnull final ComponentInfo componentInfo )
  {
    _componentInfo = Objects.requireNonNull( componentInfo );
  }

  @Nonnull
  public ComponentInfo getComponentInfo()
  {
    return _componentInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ComponentDisposeStarted" );
    map.put( "name", getComponentInfo().getName() );
  }
}
