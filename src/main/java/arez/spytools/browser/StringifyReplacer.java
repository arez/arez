package arez.spytools.browser;

import elemental2.core.JsArray;
import elemental2.core.JsObject;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jsinterop.base.Js;
import jsinterop.base.JsArrayLike;
import jsinterop.base.JsPropertyMap;

/**
 * Utility class that helps convert javascript values to strings for JSON.stringify method.
 * This class is extracted so that downstream
 */
public class StringifyReplacer
{
  private final JsArrayLike<Object> _array = new JsArray<>();

  /**
   * Return the transformed value for key-value pair.
   *
   * @param key   the name of the field.
   * @param value the value to transform.
   * @return the transformed value.
   */
  @Nullable
  public Object handleValue( @Nonnull final String key, @Nullable final Object value )
  {
    if ( null == value )
    {
      return null;
    }
    else if ( Js.typeof( value ).equals( "function" ) )
    {
      return Js.asPropertyMap( value ).get( "name" );
    }
    else if ( !Js.typeof( value ).equals( "object" ) )
    {
      return value;
    }
    else
    {
      final String v = String.valueOf( value );
      if ( null == v )
      {
        // v may be null if value.toString() returns null which will occur in optimized code in some scenarios
        return "null";
      }
      else if ( !v.startsWith( "[object " ) )
      {
        return v;
      }
      else
      {
        for ( int i = 0; i < _array.getLength(); i++ )
        {
          if ( Js.isTripleEqual( value, _array.getAtAsAny( i ) ) )
          {
            return "[Circular]";
          }
        }
        _array.setAt( _array.getLength(), value );

        final String[] propertyNames = getPropertyNames( value );
        final JsPropertyMap<Object> map = JsPropertyMap.of();
        for ( final String propertyName : propertyNames )
        {
          map.set( propertyName, Js.asPropertyMap( value ).getAsAny( propertyName ) );
        }
        return map;
      }
    }
  }

  /**
   * Return the property names that should be extracted from object when converting object.
   *
   * @param object the object value.
   * @return the property names.
   */
  @Nonnull
  protected String[] getPropertyNames( @Nonnull final Object object )
  {
    final JsArray<String> names = JsObject.getOwnPropertyNames( Js.uncheckedCast( object ) );
    return names.asArray( new String[ names.length ] );
  }
}
