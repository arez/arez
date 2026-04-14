package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when a Component dispose has completed.
 */
public final class ComponentDisposeCompleteEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComponentInfo _componentInfo;

  public ComponentDisposeCompleteEvent( @Nonnull final ComponentInfo componentInfo )
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
    map.put( "type", "ComponentDisposeComplete" );
    map.put( "name", getComponentInfo().getName() );
    SpyEventUtil.maybeAddZone( map );
  }
}
