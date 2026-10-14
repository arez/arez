package arez.doc.examples.at_memoize;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class Currency {
    private final @Nullable String _symbol;

    public Currency(final @Nullable String symbol) {
        _symbol = symbol;
    }

    public @Nullable String getSymbol() {
        return _symbol;
    }
}
