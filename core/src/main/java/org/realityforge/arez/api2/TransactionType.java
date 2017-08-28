package org.realityforge.arez.api2;

public enum TransactionType
{
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
