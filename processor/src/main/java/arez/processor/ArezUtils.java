package arez.processor;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;

final class ArezUtils
{
  private ArezUtils()
  {
  }

  public static boolean anyParametersNamed( @Nonnull final ExecutableElement element, @Nonnull final String name )
  {
    return element.getParameters().stream().anyMatch( p -> p.getSimpleName().toString().equals( name ) );
  }
}
