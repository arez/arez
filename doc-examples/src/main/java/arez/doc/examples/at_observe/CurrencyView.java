package arez.doc.examples.at_observe;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Observe;
import elemental2.dom.DomGlobal;
import elemental2.dom.Element;

@ArezComponent
public abstract class CurrencyView
{
  @CascadeDispose
  final Currency bitcoin = new Arez_Currency();

  @Observe
  void renderView()
  {
    final Element element = DomGlobal.document.getElementById( "currencyTracker" );
    element.innerHTML = "1 BTC = $" + bitcoin.getAmount() + "AUD";
  }
}
