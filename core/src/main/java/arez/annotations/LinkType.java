package arez.annotations;

import arez.Locator;
import arez.component.Linkable;

/**
 * Defines the strategy for loading references from the {@link Locator}.
 * The {@link #EAGER} strategy indicates that references should be resolved as early as
 * possible while {@link #LAZY} defers loading until required.
 */
public enum LinkType
{
  /**
   * Defines that the reference can be lazily loaded on access.
   */
  LAZY,
  /**
   * Defines that the reference is loaded via explicit {@link Linkable#link()} call.
   * The link() invocation must occur before an attempt is made to access the reference.
   */
  EXPLICIT,
  /**
   * Defines that the reference is eagerly loaded on change or during initialization.
   */
  EAGER
}
