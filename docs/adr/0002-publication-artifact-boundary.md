# Public consumers use publication archives

Public JVM imports and processor plugins consume assembled release binaries;
public J2CL libraries consume complete release source archives. This makes
downstream builds exercise the same shading, services, generated sources and
native JavaScript supplied to Maven users. Direct aliases to internal source
targets would let packaging defects escape consumer checks. Binary and source
archives remain separate after retiring GWT's source-in-binary consumption.
