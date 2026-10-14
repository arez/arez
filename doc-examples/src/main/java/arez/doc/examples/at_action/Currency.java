package arez.doc.examples.at_action;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.math.BigDecimal;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class Currency {
    private @Nullable BigDecimal _amount;

    @Observable
    public @Nullable BigDecimal getAmount() {
        return _amount;
    }

    public void setAmount(final @Nullable BigDecimal amount) {
        _amount = Objects.requireNonNull(amount);
    }
}
