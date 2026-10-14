package arez.processor;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.proton.qa.AbstractProcessorTest;

abstract class ArezProcessorTestSupport extends AbstractProcessorTest {
    @Nonnull
    @Override
    protected List<String> getOptions() {
        final List<String> options = new ArrayList<>(super.getOptions());
        options.add("--release");
        options.add("17");
        return options;
    }

    @Nonnull
    @Override
    protected String getOptionPrefix() {
        return "arez";
    }

    @Nonnull
    @Override
    protected ArezProcessor processor() {
        return new ArezProcessor();
    }
}
