package arez.doc.examples.at_action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import java.math.BigDecimal;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class Wallet {
    @CascadeDispose
    final Currency aud = new Arez_Currency();

    // A read-write action that updates observable value
    @Action
    public void updateAustralianDollarBalance(final @Nullable BigDecimal newBalance) {
        aud.setAmount(newBalance);
    }

    // A read-only action that queries observable value
    @Action(mutation = false)
    public boolean holdsAnyAustralianDollars() {
        return Objects.requireNonNull(aud.getAmount()).compareTo(BigDecimal.ZERO) > 0;
    }
}
