package arez.doc.examples.at_auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true, disposeOnDeactivate = true, requireId = Feature.ENABLE )
public abstract class Workspace
{
}
