package arez.doc.examples.at_observe;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Observe;
import akasha.Global;
import akasha.Element;

@ArezComponent
public abstract class CurrencyView
{
  @CascadeDispose
  final Currency bitcoin = new Arez_Currency();

  @Observe
  void renderView()
  {
    final Element element = Global.document().getElementById( "currencyTracker" );
    assert null != element;
    element.innerHTML = "1 BTC = $" + bitcoin.getAmount() + "AUD";
  }
}
