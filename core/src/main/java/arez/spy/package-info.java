/**
 * Spy events and introspection utilities.
 * The {@link arez} package and this package are
 * highly inter-dependent but they are kept separated as production builds
 * typically compile out everything in this package when spies are disabled.
 */
@OmitPattern( unless = "arez.enable_spies" )
package arez.spy;

import grim.annotations.OmitPattern;
