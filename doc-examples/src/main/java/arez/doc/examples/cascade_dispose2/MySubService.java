package arez.doc.examples.cascade_dispose2;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.math.BigDecimal;

@ArezComponent
public abstract class MySubService
{
  static MySubService create()
  {
    return new Arez_MySubService();
  }

  @Observable
  public abstract BigDecimal getAmount();

  public abstract void setAmount( BigDecimal amount );
}
