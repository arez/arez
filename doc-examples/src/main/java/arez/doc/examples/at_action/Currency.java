package arez.doc.examples.at_action;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.math.BigDecimal;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class Currency {
    private BigDecimal _amount = BigDecimal.ZERO;

    @Observable
    public BigDecimal getAmount() {
        return _amount;
    }

    public void setAmount(final BigDecimal amount) {
        _amount = amount;
    }
}
