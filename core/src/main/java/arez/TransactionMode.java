package arez;

/**
 * The mode of the transaction that determines if the transaction can modify state.
 */
enum TransactionMode
{
  /**
   * This transaction mode is ONLY used to dispose resources.
   * It should not be possible for an {@link Observer} to have this value as a mode.
   */
  DISPOSE,
  /**
   * Can read state within the system.
   */
  READ_ONLY,
  /**
   * Can read state within the system and can write state created/owned by the tracker.
   */
  READ_WRITE_OWNED,
  /**
   * Can read or write any observable state within the system.
   */
  READ_WRITE
}
