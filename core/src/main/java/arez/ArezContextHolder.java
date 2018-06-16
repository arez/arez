package arez;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A utility class that contains reference to singleton context when zones are disabled.
 * This is extracted to a separate class to eliminate the <clinit> from Arez and thus
 * make it much easier for GWT to optimize out code based on build time compilation parameters.
 */
final class ArezContextHolder
{
  @Nullable
  private static ArezContext c_context = Arez.areZonesEnabled() ? null : new ArezContext();

  private ArezContextHolder()
  {
  }

  /**
   * Return the ArezContext from the provider.
   *
   * @return the ArezContext.
   */
  @Nonnull
  static ArezContext context()
  {
    assert null != c_context;
    return c_context;
  }

  /**
   * cleanup context.
   * This is dangerous as it may leave dangling references and should only be done in tests.
   */
  static void reset()
  {
    c_context = new ArezContext();
  }
}
