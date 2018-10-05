package arez.doc.examples.at_observe2;

import arez.Observer;
import arez.SafeProcedure;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChanged;
import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class CurrencyView
{
  private final Currency bitcoin = new Arez_Currency();

  @Observe
  void render()
  {
    final Element element = DomGlobal.document.getElementById( "currencyTracker" );
    element.innerHTML = "1 BTC = $" + bitcoin.getAmount() + "AUD";
  }

  @OnDepsChanged
  void onRenderDepsChanged()
  {
    debounce( this::scheduleRender, 2000 );
  }

  @ObserverRef
  abstract Observer getRenderObserver();

  @Action( verifyRequired = false )
  void scheduleRender()
  {
    // Actually schedule the re-render. Has to occur within
    // an action or inside a read-write transaction
    getRenderObserver().schedule();
  }

  private void debounce( @Nonnull final SafeProcedure action, final long timeInMillis )
  {
    // Execute this action at most one every timeInMillis
    //DOC ELIDE START
    //DOC ELIDE END
  }
}
