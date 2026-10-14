package arez.doc.examples.cascade_dispose2;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.math.BigDecimal;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class MySubService {
    static @Nullable MySubService create() {
        return new Arez_MySubService();
    }

    @Observable
    public abstract @Nullable BigDecimal getAmount();

    public abstract void setAmount(@Nullable BigDecimal amount);
}
