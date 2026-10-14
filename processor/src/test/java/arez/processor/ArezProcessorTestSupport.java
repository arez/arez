package arez.processor;

import java.util.ArrayList;
import java.util.List;
import org.realityforge.proton.qa.AbstractProcessorTest;

abstract class ArezProcessorTestSupport extends AbstractProcessorTest {

    @Override
    protected List<String> getOptions() {
        final List<String> options = new ArrayList<>(super.getOptions());
        options.add("--release");
        options.add("17");
        options.add("-XDaddTypeAnnotationsToSymbol=true");
        return options;
    }

    @Override
    protected String getOptionPrefix() {
        return "arez";
    }

    @Override
    protected ArezProcessor processor() {
        return new ArezProcessor();
    }
}
