package arez.downstream;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

/**
 * A simple customization of Properties that has a stable output order basi on alphabetic ordering of keys.
 */
public final class OrderedProperties
  extends Properties
{
  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Enumeration<Object> keys()
  {
    return Collections.enumeration( new TreeSet<>( super.keySet() ) );
  }
}
