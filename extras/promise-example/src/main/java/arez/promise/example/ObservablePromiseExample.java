package arez.promise.example;

import arez.Arez;
import arez.promise.ObservablePromise;
import com.google.gwt.core.client.EntryPoint;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

public class ObservablePromiseExample
  implements EntryPoint
{
  @JsType( isNative = true, name = "Response", namespace = JsPackage.GLOBAL )
  private static class Response
  {
    @JsProperty( name = "status" )
    native int status();

    @JsProperty( name = "statusText" )
    native String statusText();
  }

  @JsType( isNative = true, name = "Error", namespace = JsPackage.GLOBAL )
  private static class JsError
  {
    @JsProperty( name = "message" )
    native String message();
  }

  @JsType( isNative = true, name = "Element", namespace = JsPackage.GLOBAL )
  private static class Element
  {
    @JsProperty
    native void setTextContent( String text );
  }

  @JsType( isNative = true, name = "Document", namespace = JsPackage.GLOBAL )
  private static class Document
  {
    @JsMethod
    native Element querySelector( String selector );
  }

  @JsType( isNative = true, name = "globalThis.console", namespace = JsPackage.GLOBAL )
  private static final class NativeConsole
  {
    @JsMethod
    private static native void log( Object message );
  }

  @Override
  public void onModuleLoad()
  {
    final ObservablePromise.Promise<Response> fetch =
      fetch( "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise" );
    final ObservablePromise<Response, Object> observablePromise = ObservablePromise.create( fetch );
    Arez.context().observer( () -> outputStatus( observablePromise ) );
  }

  private void outputStatus( @Nonnull final ObservablePromise<Response, Object> observablePromise )
  {
    final ObservablePromise.State state = observablePromise.getState();
    final Response response = ObservablePromise.State.FULFILLED == state ? observablePromise.getValue() : null;
    final JsError error =
      ObservablePromise.State.REJECTED == state ? Js.cast( observablePromise.getError() ) : null;
    final String message =
      "Promise State: " + state +
      ( null != response ? " - Response: " + response.status() + ": " + response.statusText() : "" ) +
      ( null != error ? " - Error: " + error.message() : "" );
    NativeConsole.log( message );
    final Element element = document().querySelector( "#app" );
    assert null != element;
    element.setTextContent( message );
  }

  @JsMethod( name = "fetch", namespace = JsPackage.GLOBAL )
  private static native ObservablePromise.Promise<Response> fetch( String url );

  @JsProperty( name = "document", namespace = JsPackage.GLOBAL )
  private static native Document document();
}
