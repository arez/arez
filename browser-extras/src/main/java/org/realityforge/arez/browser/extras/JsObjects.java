package org.realityforge.arez.browser.extras;

final class JsObjects
{
  static native <T> T get( Object object, String propertyName ) /*-{
    return object[ propertyName ];
  }-*/;

  static native void set( Object object, String propertyName, Object value ) /*-{
    object[ propertyName ] = value;
  }-*/;

  private JsObjects()
  {
  }
}
