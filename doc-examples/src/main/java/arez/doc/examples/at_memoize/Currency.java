package arez.doc.examples.at_memoize;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class Currency {
    private final String _symbol;

    public Currency(final String symbol) {
        _symbol = symbol;
    }

    public String getSymbol() {
        return _symbol;
    }
}
