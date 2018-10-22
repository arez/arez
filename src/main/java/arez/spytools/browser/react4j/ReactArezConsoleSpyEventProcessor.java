package arez.spytools.browser.react4j;

import arez.spytools.browser.ConsoleSpyEventProcessor;
import arez.spytools.browser.StringifyReplacer;
import java.util.Arrays;
import javax.annotation.Nonnull;

/**
 * A customized console event processor that avoids accessing "key" and "ref" attributes.
 * This causes warnings when accessing props.key and props.ref in react components.
 */
public class ReactArezConsoleSpyEventProcessor
  extends ConsoleSpyEventProcessor
{
  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  protected StringifyReplacer getStringifyReplacer()
  {
    return new StringifyReplacer()
    {
      @Nonnull
      @Override
      protected String[] getPropertyNames( @Nonnull final Object object )
      {
        return Arrays.stream( super.getPropertyNames( object ) ).
          filter( n -> !n.equals( "key" ) && !n.equals( "ref" ) )
          .toArray( String[]::new );
      }
    };
  }
}
