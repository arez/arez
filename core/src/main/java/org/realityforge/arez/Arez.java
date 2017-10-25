package org.realityforge.arez;

import javax.annotation.Nonnull;
import org.realityforge.anodoc.TestOnly;
import static org.realityforge.braincheck.Guards.*;

/**
 * Support class to provide access to instances of ArezContext.
 * The {@link ContextProvider} that is bound to this class will determine how this
 * achieved but it may involve using Zones (in a JavaScript runtime) or ThreadLocals
 * (on the JVM). The default implementation provides a singleton Context.
 */
public final class Arez
{
  /**
   * The provider that used to access context.
   */
  private static ContextProvider c_provider;

  private Arez()
  {
  }

  /**
   * Interface for supplying an instance of an ArezContext to the caller.
   */
  @FunctionalInterface
  public interface ContextProvider
  {
    /**
     * Return a ArezContext based on the providers particular strategy.
     *
     * @return the ArezContext.
     */
    @Nonnull
    ArezContext context();
  }

  /**
   * Return true if zones are enabled, false otherwise.
   *
   * @return true if zones are enabled, false otherwise.
   */
  public static boolean areZonesEnabled()
  {
    return ArezConfig.enableZones();
  }

  /**
   * Return true if user should pass names into API methods, false if should pass null.
   *
   * @return true if user should pass names into API methods, false if should pass null.
   */
  public static boolean areNamesEnabled()
  {
    return ArezConfig.enableNames();
  }

  /**
   * Return true if spies are enabled.
   *
   * @return true if spies are enabled, false otherwise.
   */
  public static boolean areSpiesEnabled()
  {
    return ArezConfig.enableSpy();
  }

  /**
   * Return the ArezContext from the provider.
   *
   * @return the ArezContext.
   */
  @Nonnull
  public static ArezContext context()
  {
    return getContextProvider().context();
  }

  /**
   * Bind a ContextProvider to Arez.
   * This method should not be called if a provider has already been bound
   * or the method {@link #context()} has already been called (and created
   * the default provider).
   *
   * @param provider the ContextProvider to bind.
   */
  public static void bindProvider( @Nonnull final ContextProvider provider )
  {
    apiInvariant( () -> null == c_provider,
                  () -> "Attempting to bind ContextProvider " + provider + " but there is already a " +
                        "provider bound as " + c_provider + "." );
    c_provider = provider;
  }

  /**
   * Return the ContextProvider creating the default provider if necessary.
   *
   * @return the ContextProvider.
   */
  @Nonnull
  synchronized static ContextProvider getContextProvider()
  {
    if ( null == c_provider )
    {
      c_provider = new StaticContextProvider();
    }
    return c_provider;
  }

  /**
   * Clear the provider field.
   * This is dangerous as it may leave dangling references and should onkly be done in tests.
   */
  @TestOnly
  static void clearProvider()
  {
    c_provider = null;
  }

  /**
   * Default implementation of context provider that just returns a singleton context.
   */
  static final class StaticContextProvider
    implements ContextProvider
  {
    private final ArezContext _context = new ArezContext();

    /**
     * {@inheritDoc}
     */
    @Nonnull
    public ArezContext context()
    {
      return _context;
    }
  }
}
