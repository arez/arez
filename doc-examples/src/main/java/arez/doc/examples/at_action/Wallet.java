package arez.doc.examples.at_action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import java.math.BigDecimal;

@ArezComponent
public class Wallet
{
  private final Currency aud = new Currency();

  // A read-write action that updates observable value
  @Action
  public void updateAustralianDollarBalance( final BigDecimal newBalance )
  {
    aud.setAmount( newBalance );
  }

  // A read-only action that queries observable value
  @Action( mutation = false )
  public boolean holdsAnyAustralianDollars()
  {
    return aud.getAmount().compareTo( BigDecimal.ZERO ) > 0;
  }
}
