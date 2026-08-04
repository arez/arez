package arez.processor;

import javax.annotation.Nonnull;
import org.realityforge.proton.qa.AbstractProcessorTest;

abstract class ArezProcessorTestSupport
  extends AbstractProcessorTest
{
  @Nonnull
  @Override
  protected String getOptionPrefix()
  {
    return "arez";
  }

  @Nonnull
  @Override
  protected ArezProcessor processor()
  {
    return new ArezProcessor();
  }
}
