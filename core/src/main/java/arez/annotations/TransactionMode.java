package arez.annotations;

/**
 * Enum describing the required mode of an existing transaction.
 */
public enum TransactionMode
{
  /**
   * Any active transaction is acceptable.
   */
  ANY,
  /**
   * A read-only transaction is required.
   */
  READ_ONLY,
  /**
   * A read-write transaction is required.
   */
  READ_WRITE
}
