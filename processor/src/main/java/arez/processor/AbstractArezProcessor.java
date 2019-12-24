package arez.processor;

import arez.processor.support.AbstractStandardProcessor;
import javax.annotation.Nonnull;

/**
 * Abstract base annotation processor for Arez.
 */
public abstract class AbstractArezProcessor
  extends AbstractStandardProcessor
{
  @Override
  @Nonnull
  protected final String getIssueTrackerURL()
  {
    return "https://github.com/arez/arez/issues";
  }

  @Nonnull
  @Override
  protected final String getOptionPrefix()
  {
    return "arez";
  }
}
