package org.realityforge.arez.annotations;

/**
 * Enum to control when injectible elements should be present.
 */
public enum Injectible
{
  /**
   * Feature should be present.
   */
  TRUE,
  /**
   * Feature should not be present.
   */
  FALSE,
  /**
   * Feature should be present if supporting infrastructure is detected.
   */
  IF_DETECTED
}
