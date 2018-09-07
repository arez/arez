package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when a Component is starting to be created.
 */
public final class ComponentCreateStartedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComponentInfo _componentInfo;

  public ComponentCreateStartedEvent( @Nonnull final ComponentInfo componentInfo )
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
    map.put( "type", "ComponentCreateStarted" );
    map.put( "component", getComponentInfo().getName() );
  }
}
