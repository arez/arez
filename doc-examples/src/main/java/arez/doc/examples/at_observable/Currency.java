package arez.doc.examples.at_observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.math.BigDecimal;
import java.util.Objects;

@ArezComponent
public class Currency
{
  private BigDecimal _amount;

  @Observable( name = "amount" )
  public BigDecimal amount()
  {
    return _amount;
  }

  @Observable( name = "amount" )
  public void updateAmount( final BigDecimal amount )
  {
    _amount = Objects.requireNonNull( amount );
  }

  //DOC ELIDE START
  //DOC ELIDE END
}
