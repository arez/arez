# Make repository scans opt-in

Repository subclasses choose which scans to expose because a scan observes whole-repository
membership and can visit every loaded entity. Protected scan helpers have distinct names so
public wrappers can call them directly. Closure's devirtualization pass excludes method bodies
that contain `super`. [Closure source](https://github.com/google/closure-compiler/blob/v20260913/src/com/google/javascript/jscomp/DevirtualizeMethods.java#L276-L280)

Each helper can be overridden independently. The base implementations share private methods for
membership observation and first-match lookup, so inherited scans retain their behavior when a
subclass overrides another helper.
