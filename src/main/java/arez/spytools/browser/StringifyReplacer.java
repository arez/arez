package arez.spytools.browser;

import akasha.core.JsObject;
import akasha.lang.JsArray;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsArrayLike;
import jsinterop.base.JsPropertyMap;

/**
 * Utility class that helps convert javascript values to strings for JSON.stringify method.
 * This class is extracted so that downstream
 */
public class StringifyReplacer
{
  @Nonnull
  private final JsArrayLike<Object> _array = new JsArray<>();

  /**
   * Return the transformed value for key-value pair.
   *
   * @param value the value to transform.
   * @return the transformed value.
   */
  @Nullable
  public Any handleValue( @Nullable final Object value )
  {
    if ( null == value )
    {
      return null;
    }
    else if ( Js.typeof( value ).equals( "function" ) )
    {
      return Js.asPropertyMap( value ).getAsAny( "name" );
    }
    else if ( !Js.typeof( value ).equals( "object" ) )
    {
      return Js.asAny( value );
    }
    else
    {
      final String v = String.valueOf( value );
      if ( null == v )
      {
        // v may be null if value.toString() returns null which will occur in optimized code in some scenarios
        return Js.asAny( "null" );
      }
      else if ( !v.startsWith( "[object " ) )
      {
        return Js.asAny( v );
      }
      else
      {
        for ( int i = 0; i < _array.getLength(); i++ )
        {
          if ( Js.isTripleEqual( value, _array.getAtAsAny( i ) ) )
          {
            return Js.asAny( "[Circular]" );
          }
        }
        _array.setAt( _array.getLength(), value );

        final String[] propertyNames = getPropertyNames( value );
        final JsPropertyMap<Object> map = JsPropertyMap.of();
        for ( final String propertyName : propertyNames )
        {
          map.set( propertyName, Js.asPropertyMap( value ).getAsAny( propertyName ) );
        }
        return Js.asAny( map );
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
    final JsArray<String> names = JsObject.getOwnPropertyNames( object );
    return names.asArray( new String[ names.length ] );
  }
}
