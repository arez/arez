package arez.spy;

import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Notification when a reaction cycle starts.
 */
public final class ReactionCycleStartEvent
  implements SerializableEvent
{
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ReactionCycleStart" );
    SpyEventUtil.maybeAddZone( map );
  }
}
