package arez.doc.examples.at_observe2;

import akasha.Element;
import akasha.Global;
import arez.Observer;
import arez.SafeProcedure;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class CurrencyView
{
  @CascadeDispose
  final Currency bitcoin = new Arez_Currency();

  @Observe
  void render()
  {
    final Element element = Global.document().getElementById( "currencyTracker" );
    assert null != element;
    element.innerHTML = "1 BTC = $" + bitcoin.getAmount() + "AUD";
  }

  @OnDepsChange
  void onRenderDepsChange( @Nonnull final Observer observer )
  {
    debounce( observer::schedule, 2000 );
  }

  private void debounce( @Nonnull final SafeProcedure action, final long timeInMillis )
  {
    // Execute this action at most one every timeInMillis
    //DOC ELIDE START
    //DOC ELIDE END
  }
}
