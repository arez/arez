package arez.doc.examples.sting;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent(sting = Feature.ENABLE)
public abstract class MyService {
    @Action
    public void performAction(final int value) {}
}
