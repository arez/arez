package arez.persist.processor;

import arez.processor.ArezProcessor;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import org.realityforge.proton.qa.AbstractProcessorTest;

abstract class ArezPersistProcessorTestSupport
  extends AbstractProcessorTest
{
  @Nonnull
  @Override
  protected List<String> getOptions()
  {
    final List<String> options = new ArrayList<>( super.getOptions() );
    options.add( "--release" );
    options.add( "17" );
    return options;
  }

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
