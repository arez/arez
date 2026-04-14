package arez.integration.util;

import arez.spy.SerializableEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.json.Json;
import javax.json.JsonObject;

public final class SpyEventTestUtil
{
  private SpyEventTestUtil()
  {
  }

  @Nonnull
  public static JsonObject toJsonObject( @Nonnull final SerializableEvent event, final boolean keepValue )
  {
    final Map<String, Object> map = new HashMap<>();
    event.toMap( map );
    map.remove( "duration" );
    map.remove( "zone" );
    if ( !keepValue )
    {
      map.remove( "value" );
    }
    final Map<String, Object> output = new HashMap<>();
    for ( final Map.Entry<String, Object> entry : map.entrySet() )
    {
      final String key = entry.getKey();
      final Object value = entry.getValue();
      if ( key.equals( "value" ) && value instanceof Collection )
      {
        // Useful for debugging repositories
        output.put( key, ( (Collection<?>) value ).size() + " items" );
      }
      else if ( null != value && value.getClass().isArray() )
      {
        final ArrayList<Object> v = new ArrayList<>();
        for ( int i = 0, end = Array.getLength( value ); i < end; i++ )
        {
          v.add( Array.get( value, i ) );
        }
        output.put( key, v );
      }
      else
      {
        output.put( key, value );
      }
    }
    return Json.createObjectBuilder( output ).build();
  }
}
