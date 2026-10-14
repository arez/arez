package arez.doc.examples.at_observable;

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

    @Observable(name = "amount")
    public @Nullable BigDecimal amount() {
        return _amount;
    }

    @Observable(name = "amount")
    public void updateAmount(final @Nullable BigDecimal amount) {
        _amount = Objects.requireNonNull(amount);
    }

    // DOC ELIDE START
    // DOC ELIDE END
}
