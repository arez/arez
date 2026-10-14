package arez.doc.examples.at_auto_observe;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent(allowEmpty = true, disposeOnDeactivate = true)
public abstract class Session {}
