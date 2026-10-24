package arez.persist.processor;

import arez.processor.ArezProcessor;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Processor;
import org.realityforge.proton.qa.AbstractProcessorTest;

abstract class ArezPersistProcessorTestSupport extends AbstractProcessorTest {

    @Override
    protected List<String> getOptions() {
        final List<String> options = new ArrayList<>(super.getOptions());
        options.add("--release");
        options.add("21");
        options.add("-XDaddTypeAnnotationsToSymbol=true");
        return options;
    }

    @Override
    protected String getOptionPrefix() {
        return "arez.persist";
    }

    @Override
    protected ArezPersistProcessor processor() {
        return new ArezPersistProcessor();
    }

    @Override
    protected Processor[] additionalProcessors() {
        return new Processor[] {new ArezProcessor()};
    }

    @Override
    protected boolean emitGeneratedFile(final String target) {
        return super.emitGeneratedFile(target)
                && !target.contains("/Arez_")
                && !target.contains("_Arez_")
                && !target.startsWith("Arez_");
    }
}
