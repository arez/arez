package arez.doc.examples.at_memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ArezComponent
public abstract class CurrencyCollection
{
  @Observable
  public String getFilter()
  {
    // Return value used to filter currencies by their symbol
    //DOC ELIDE START
    return _filter;
    //DOC ELIDE END
  }

  @Observable
  public List<Currency> getCurrencies()
  {
    // Return the list of all currencies here
    //DOC ELIDE START
    return _currencies;
    //DOC ELIDE END
  }

  // Memoized method that only changes when a currency is added or removed
  @Memoize
  public int getCurrencyCount()
  {
    return getCurrencies().size();
  }

  // Memoized method that changes when a currency is added or removed,
  // or the filter changes
  @Memoize
  public List<Currency> filteredCurrencies()
  {
    return getCurrencies()
      .stream()
      .filter( c -> c.getSymbol().contains( getFilter() ) )
      .collect( Collectors.toList() );
  }

  //DOC ELIDE START
  private List<Currency> _currencies = new ArrayList<>();
  private String _filter;

  public void setFilter( final String filter )
  {
    _filter = filter;
  }

  public void setCurrencies( final List<Currency> currencies )
  {
    _currencies = currencies;
  }

  //DOC ELIDE END
}
