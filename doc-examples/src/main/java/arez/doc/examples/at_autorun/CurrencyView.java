package arez.doc.examples.at_autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import elemental2.dom.DomGlobal;
import elemental2.dom.Element;

@ArezComponent
public abstract class CurrencyView
{
  private final Currency bitcoin = new Arez_Currency();

  @Autorun
  void renderView()
  {
    final Element element = DomGlobal.document.getElementById( "currencyTracker" );
    element.innerHTML = "1 BTC = $" + bitcoin.getAmount() + "AUD";
  }
}
