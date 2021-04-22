package arez.doc.examples.at_observe;

import akasha.Element;
import akasha.WindowGlobal;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Observe;

@ArezComponent
public abstract class CurrencyView
{
  @CascadeDispose
  final Currency bitcoin = new Arez_Currency();

  @Observe
  void renderView()
  {
    final Element element = WindowGlobal.document().getElementById( "currencyTracker" );
    assert null != element;
    element.innerHTML = "1 BTC = $" + bitcoin.getAmount() + "AUD";
  }
}
