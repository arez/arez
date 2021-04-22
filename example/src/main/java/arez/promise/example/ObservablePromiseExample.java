package arez.promise.example;

import akasha.Console;
import akasha.Element;
import akasha.Response;
import akasha.WindowGlobal;
import akasha.core.JsError;
import akasha.promise.Promise;
import arez.Arez;
import arez.promise.ObservablePromise;
import com.google.gwt.core.client.EntryPoint;
import javax.annotation.Nonnull;
import jsinterop.base.Js;

public class ObservablePromiseExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final Promise<Response> fetch =
      WindowGlobal.fetch( "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise" );
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
    Console.log( message );
    final Element element = WindowGlobal.document().querySelector( "#app" );
    assert null != element;
    element.textContent = message;
  }
}
