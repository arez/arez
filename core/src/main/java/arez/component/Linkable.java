package arez.component;

import javax.annotation.Nonnull;

/**
 * Interface implemented by components that have references that need to be eagerly resolved.
 * The method on this interface should only be invoked by the runtime and not directly by external code.
 */
public interface Linkable
{
  /**
   * Resolve any references.
   */
  void link();

  /**
   * Link specified object if it is linkable.
   *
   * @param object the object to link if linkable.
   */
  static void link( @Nonnull final Object object )
  {
    if ( object instanceof Linkable )
    {
      final Linkable linkable = (Linkable) object;
      linkable.link();
    }
  }
}
