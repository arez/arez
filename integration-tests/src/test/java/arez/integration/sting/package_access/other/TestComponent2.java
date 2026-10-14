package arez.integration.sting.package_access.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent(allowEmpty = true, sting = Feature.ENABLE)
public abstract class TestComponent2 {
    TestComponent2(final @Nullable MyDependency myDependency, final @Nullable MyDependency2 myOtherDependency) {}

    @PostConstruct
    void postConstruct() {}
}
