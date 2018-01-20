package arez.doc.examples.at_autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.doc.examples.at_action.Currency;
import elemental2.dom.DomGlobal;
import elemental2.dom.Element;

@ArezComponent
public class CurrencyView
{
  private final Currency bitcoin = new Currency();

  @Autorun( mutation = false )
  public void renderView()
  {
    final Element element = DomGlobal.document.getElementById( "currencyTracker" );
    element.innerHTML = "1 BTC = $" + bitcoin.getAmount() + "AUD";
  }
}
