package arez.integration.sting.package_access.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent(sting = Feature.ENABLE)
public abstract class TestComponent {
    TestComponent(final @Nullable MyDependency myDependency) {}

    @PostConstruct
    void postConstruct() {}

    @Observable
    abstract @Nullable String getValue();

    abstract void setValue(@Nullable String value);
}
