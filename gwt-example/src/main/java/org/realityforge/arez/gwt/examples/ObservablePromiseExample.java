package org.realityforge.arez.gwt.examples;

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import javax.annotation.Nonnull;
import jsinterop.base.Js;
import org.realityforge.arez.Arez;
import org.realityforge.arez.browser.extras.ObservablePromise;

public class ObservablePromiseExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final Promise<Response> fetch =
      DomGlobal.fetch( "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise" );
    final ObservablePromise<Response, Object> observablePromise = ObservablePromise.create( fetch );
    Arez.context().autorun( () -> outputStatus( observablePromise ) );
  }

  private void outputStatus( @Nonnull final ObservablePromise<Response, Object> observablePromise )
  {
    final ObservablePromise.State state = observablePromise.getState();
    final Response response = ObservablePromise.State.FULFILLED == state ? observablePromise.getValue() : null;
    final elemental2.core.Error error =
      ObservablePromise.State.REJECTED == state ? Js.cast( observablePromise.getError() ) : null;
    final String message =
      "Promise State: " + state +
      ( null != response ? " - Response: " + response.status + ": " + response.statusText : "" ) +
      ( null != error ? " - Error: " + error.message : "" );
    DomGlobal.console.log( message );
    DomGlobal.document.querySelector( "#app" ).textContent = message;
  }
}
