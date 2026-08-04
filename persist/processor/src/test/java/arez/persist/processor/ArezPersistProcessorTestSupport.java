package arez.persist.processor;

import arez.processor.ArezProcessor;
import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import org.realityforge.proton.qa.AbstractProcessorTest;

abstract class ArezPersistProcessorTestSupport
  extends AbstractProcessorTest
{
  @Nonnull
  @Override
  protected String getOptionPrefix()
  {
    return "arez.persist";
  }

  @Nonnull
  @Override
  protected ArezPersistProcessor processor()
  {
    return new ArezPersistProcessor();
  }

  @Nonnull
  @Override
  protected Processor[] additionalProcessors()
  {
    return new Processor[]{ new ArezProcessor() };
  }

  @Override
  protected boolean emitGeneratedFile( @Nonnull final String target )
  {
    return super.emitGeneratedFile( target ) &&
           !target.contains( "/Arez_" ) &&
           !target.contains( "_Arez_" ) &&
           !target.startsWith( "Arez_" );
  }
}
