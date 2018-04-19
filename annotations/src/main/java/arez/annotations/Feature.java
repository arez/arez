package arez.annotations;

/**
 * Enum to control if a feature should be enabled.
 */
public enum Feature
{
  /**
   * Feature should be present.
   */
  ENABLE,
  /**
   * Feature should not be present.
   */
  DISABLE,
  /**
   * Feature should be present if autodetect heuristics determines that it should be enables.
   */
  AUTODETECT
}
