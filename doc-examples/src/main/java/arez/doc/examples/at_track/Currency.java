package arez.doc.examples.at_track;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.math.BigDecimal;
import java.util.Objects;

@ArezComponent
public abstract class Currency
{
  private BigDecimal _amount;

  @Observable
  public BigDecimal getAmount()
  {
    return _amount;
  }

  public void setAmount( final BigDecimal amount )
  {
    _amount = Objects.requireNonNull( amount );
  }
}
