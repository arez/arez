package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when a Component is disposed.
 */
public final class ComponentDisposeStartEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComponentInfo _componentInfo;

  public ComponentDisposeStartEvent( @Nonnull final ComponentInfo componentInfo )
  {
    _componentInfo = Objects.requireNonNull( componentInfo );
  }

  @Nonnull
  public ComponentInfo getComponentInfo()
  {
    return _componentInfo;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ComponentDisposeStart" );
    map.put( "name", getComponentInfo().getName() );
    SpyEventUtil.maybeAddZone( map );
  }
}
