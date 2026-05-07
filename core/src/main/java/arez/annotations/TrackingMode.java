package arez.annotations;

/**
 * Enum describing the required tracking state of an existing transaction.
 */
public enum TrackingMode
{
  /**
   * Any active transaction is acceptable.
   */
  ANY,
  /**
   * A tracking transaction is required.
   */
  TRACKING,
  /**
   * A non-tracking transaction is required.
   */
  NON_TRACKING
}
