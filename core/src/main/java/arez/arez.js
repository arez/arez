/**
 * This file provides the @defines for arez configuration options.
 * See ArezConfig.java for details.
 */
goog.provide('arez');

/** @define {string} */
arez.environment = goog.define('arez.environment', 'production');

/** @define {string} */
arez.enable_names = goog.define('arez.enable_names', 'false');

/** @define {string} */
arez.enable_references = goog.define('arez.enable_references', 'true');

/** @define {string} */
arez.enable_property_introspection = goog.define('arez.enable_property_introspection', 'false');

/** @define {string} */
arez.enable_verify = goog.define('arez.enable_verify', 'false');

/** @define {string} */
arez.enforce_transaction_type = goog.define('arez.enforce_transaction_type', 'false');

/** @define {string} */
arez.purge_tasks_when_runaway_detected = goog.define('arez.purge_tasks_when_runaway_detected', 'true');

/** @define {string} */
arez.collections_properties_unmodifiable = goog.define('arez.collections_properties_unmodifiable', 'false');

/** @define {string} */
arez.enable_zones = goog.define('arez.enable_zones', 'false');

/** @define {string} */
arez.enable_spies = goog.define('arez.enable_spies', 'false');

/** @define {string} */
arez.enable_native_components = goog.define('arez.enable_native_components', 'false');

/** @define {string} */
arez.enable_registries = goog.define('arez.enable_registries', 'false');

/** @define {string} */
arez.enable_observer_error_handlers = goog.define('arez.enable_observer_error_handlers', 'true');

/** @define {string} */
arez.enable_task_interceptor = goog.define('arez.enable_task_interceptor', 'true');

/** @define {string} */
arez.check_invariants = goog.define('arez.check_invariants', 'false');

/** @define {string} */
arez.check_expensive_invariants = goog.define('arez.check_expensive_invariants', 'false');

/** @define {string} */
arez.check_api_invariants = goog.define('arez.check_api_invariants', 'false');

/** @define {string} */
arez.logger = goog.define('arez.logger', 'none');
