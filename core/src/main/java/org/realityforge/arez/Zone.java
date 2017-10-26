package org.realityforge.arez;

import javax.annotation.Nonnull;

/**
 * An isolated Arez context.
 */
public final class Zone
{
  /**
   * The underlying context for zone.
   */
  private final ArezContext _context = new ArezContext();

  /**
   * Return the context for the zone.
   *
   * @return the context for the zone.
   */
  @Nonnull
  public ArezContext getContext()
  {
    return _context;
  }

  /**
   * Create a zone.
   * Should only be done via {@link Arez} methods.
   */
  Zone()
  {
  }

  public boolean isActive()
  {
    return Arez.currentZone() == this;
  }

  /**
   * Save the old zone and make this the current zone.
   */
  public void activate()
  {
    Arez.activateZone( this );
  }

  /**
   * Restore the old zone.
   * This takes the zone that was current when {@link #activate()} was called on this zone
   * and restores it to being the current zone.
   */
  public void deactivate()
  {
    Arez.deactivateZone( this );
  }
}
