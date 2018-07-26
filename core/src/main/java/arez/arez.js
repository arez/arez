/**
 * This file provides the @defines for arez configuration options.
 * See ArezConfig.java for details.
 */
goog.provide('arez');

/** @define {string} */
goog.define('arez.environment', 'production');

/** @define {string} */
goog.define('arez.enable_names', 'false');

/** @define {string} */
goog.define('arez.enable_property_introspection', 'false');

/** @define {string} */
goog.define('arez.enforce_transaction_type', 'false');

/** @define {string} */
goog.define('arez.purge_reactions_when_runaway_detected', 'true');

/** @define {string} */
goog.define('arez.collections_properties_unmodifiable', 'false');

/** @define {string} */
goog.define('arez.enable_zones', 'false');

/** @define {string} */
goog.define('arez.enable_spies', 'false');

/** @define {string} */
goog.define('arez.enable_native_components', 'false');

/** @define {string} */
goog.define('arez.enable_registries', 'false');

/** @define {string} */
goog.define('arez.enable_observer_error_handlers', 'false');

/** @define {string} */
goog.define('arez.check_invariants', 'false');

/** @define {string} */
goog.define('arez.check_api_invariants', 'false');

/** @define {string} */
goog.define('arez.logger', 'none');
