package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when a Component is has finished being created.
 */
public final class ComponentCreateCompletedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ComponentCreateCompletedEvent.class );
  @Nonnull
  private final ComponentInfo _componentInfo;

  public ComponentCreateCompletedEvent( @Nonnull final ComponentInfo componentInfo )
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
    map.put( "type", TYPE_NAME );
    map.put( "component", getComponentInfo().getName() );
  }
}
