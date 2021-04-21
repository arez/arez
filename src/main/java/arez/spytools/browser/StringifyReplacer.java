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
  public final Any handleValue( @Nullable final Any value )
  {
    if ( null == value )
    {
      return null;
    }
    else
    {
      final String typeof = Js.typeof( value );
      if ( typeof.equals( "function" ) )
      {
        return Js.asPropertyMap( value ).getAsAny( "name" );
      }
      else if ( !typeof.equals( "object" ) )
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

          final JsArray<String> names = JsObject.getOwnPropertyNames( value );
          final String[] propertyNames = names.asArray( new String[ names.length ] );
          final JsPropertyMap<Object> map = JsPropertyMap.of();
          for ( final String propertyName : propertyNames )
          {
            if( includeProperty(value, propertyName) )
            map.set( propertyName, Js.asPropertyMap( value ).getAsAny( propertyName ) );
          }
          return Js.asAny( map );
        }
      }
    }
  }

  protected boolean includeProperty( @Nonnull final Any value, @Nonnull final String propertyName )
  {
    return true;
  }
}
