package arez.doc.examples.at_memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class CurrencyCollection {
    @Observable
    public @Nullable String getFilter() {
        // Return value used to filter currencies by their symbol
        // DOC ELIDE START
        return _filter;
        // DOC ELIDE END
    }

    @Observable
    public @Nullable List<Currency> getCurrencies() {
        // Return the list of all currencies here
        // DOC ELIDE START
        return _currencies;
        // DOC ELIDE END
    }

    // Memoized method that only changes when a currency is added or removed
    @Memoize
    public int getCurrencyCount() {
        return Objects.requireNonNull(getCurrencies()).size();
    }

    // Memoized method that changes when a currency is added or removed,
    // or the filter changes
    @Memoize
    public @Nullable List<Currency> filteredCurrencies() {
        return Objects.requireNonNull(getCurrencies()).stream()
                .filter(c -> Objects.requireNonNull(c.getSymbol()).contains(Objects.requireNonNull(getFilter())))
                .collect(Collectors.toList());
    }

    // DOC ELIDE START
    private @Nullable List<Currency> _currencies = new ArrayList<>();
    private @Nullable String _filter;

    public void setFilter(final @Nullable String filter) {
        _filter = filter;
    }

    public void setCurrencies(final @Nullable List<Currency> currencies) {
        _currencies = currencies;
    }

    // DOC ELIDE END
}
