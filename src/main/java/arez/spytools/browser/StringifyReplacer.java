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
    else if ( isJavaClass( value ) )
    {
      return String.valueOf( value );
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
      for ( int i = 0; i < _array.getLength(); i++ )
      {
        if ( Js.isTripleEqual( value, _array.getAnyAt( i ) ) )
        {
          return "[Circular]";
        }
      }
      _array.setAt( _array.getLength(), value );

      final String[] propertyNames = getPropertyNames( value );
      final JsPropertyMap<Object> map = JsPropertyMap.of();
      for ( final String propertyName : propertyNames )
      {
        map.set( propertyName, Js.asPropertyMap( value ).getAny( propertyName ) );
      }
      return map;
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
    return JsObject.getOwnPropertyNames( Js.uncheckedCast( object ) );
  }

  /**
   * Return true if the specified object has an associated java class constant.
   */
  private native boolean isJavaClass( @Nonnull final Object object ) /*-{
    return undefined !== object.__proto__ && undefined !== object.__proto__.@java.lang.Object::___clazz;
  }-*/;
}
