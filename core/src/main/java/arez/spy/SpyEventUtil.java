package arez.spy;

import arez.Arez;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Utility methods to assist spy event serialization.
 */
final class SpyEventUtil
{
  private SpyEventUtil()
  {
  }

  /**
   * If zones and names are enabled, add the current zone name to the serialized event map.
   *
   * @param map the map to populate.
   */
  static void maybeAddZone( @Nonnull final Map<String, Object> map )
  {
    if ( Arez.areZonesEnabled() )
    {
      map.put( "zone", Arez.currentZone().getName() );
    }
  }
}
