package arez;

import grim.annotations.OmitType;
import javax.annotation.Nonnull;

/**
 * An isolated Arez context.
 */
@OmitType( unless = "arez.enable_zones" )
public final class Zone
{
  /**
   * The underlying context for zone.
   */
  private final ArezContext _context = new ArezContext( this );

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
   * Run the specified function in the zone.
   * Activate the zone on entry, deactivate on exit.
   *
   * @param <T>    The type of the value returned from function.
   * @param action the function to execute.
   * @return the value returned from function.
   */
  public <T> T safeRun( @Nonnull final SafeFunction<T> action )
  {
    Arez.activateZone( this );
    try
    {
      return action.call();
    }
    finally
    {
      Arez.deactivateZone( this );
    }
  }

  /**
   * Run the specified function in the zone.
   * Activate the zone on entry, deactivate on exit.
   *
   * @param <T>    The type of the value returned from function.
   * @param action the function to execute.
   * @return the value returned from function.
   * @throws Throwable if the function throws an exception.
   */
  public <T> T run( @Nonnull final Function<T> action )
    throws Throwable
  {
    Arez.activateZone( this );
    try
    {
      return action.call();
    }
    finally
    {
      Arez.deactivateZone( this );
    }
  }

  /**
   * Run the specified procedure in the zone.
   * Activate the zone on entry, deactivate on exit.
   *
   * @param action the procedure to execute.
   */
  public void safeRun( @Nonnull final SafeProcedure action )
  {
    Arez.activateZone( this );
    try
    {
      action.call();
    }
    finally
    {
      Arez.deactivateZone( this );
    }
  }

  /**
   * Run the specified procedure in the zone.
   * Activate the zone on entry, deactivate on exit.
   *
   * @param action the procedure to execute.
   * @throws Throwable if the procedure throws an exception.
   */
  public void run( @Nonnull final Procedure action )
    throws Throwable
  {
    Arez.activateZone( this );
    try
    {
      action.call();
    }
    finally
    {
      Arez.deactivateZone( this );
    }
  }
}
