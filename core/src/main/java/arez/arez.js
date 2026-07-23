/**
 * This file provides the @defines for arez configuration options.
 * See ArezConfig.java for details.
 */
goog.provide('arez');
goog.require('jre');

/** @define {string} */
arez.environment = goog.define('arez.environment', 'production');
goog.module.get('jre').addSystemPropertyFromGoogDefine('arez.environment', arez.environment);

/** @define {string} */
arez.enable_names = goog.define('arez.enable_names', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine('arez.enable_names', arez.enable_names);

/** @define {string} */
arez.enable_references = goog.define('arez.enable_references', 'true');
goog.module.get('jre').addSystemPropertyFromGoogDefine('arez.enable_references', arez.enable_references);

/** @define {string} */
arez.enable_property_introspection = goog.define('arez.enable_property_introspection', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine(
  'arez.enable_property_introspection',
  arez.enable_property_introspection);

/** @define {string} */
arez.enable_verify = goog.define('arez.enable_verify', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine('arez.enable_verify', arez.enable_verify);

/** @define {string} */
arez.enforce_transaction_type = goog.define('arez.enforce_transaction_type', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine(
  'arez.enforce_transaction_type',
  arez.enforce_transaction_type);

/** @define {string} */
arez.purge_tasks_when_runaway_detected = goog.define('arez.purge_tasks_when_runaway_detected', 'true');
goog.module.get('jre').addSystemPropertyFromGoogDefine(
  'arez.purge_tasks_when_runaway_detected',
  arez.purge_tasks_when_runaway_detected);

/** @define {string} */
arez.collections_properties_unmodifiable = goog.define('arez.collections_properties_unmodifiable', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine(
  'arez.collections_properties_unmodifiable',
  arez.collections_properties_unmodifiable);

/** @define {string} */
arez.enable_zones = goog.define('arez.enable_zones', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine('arez.enable_zones', arez.enable_zones);

/** @define {string} */
arez.enable_spies = goog.define('arez.enable_spies', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine('arez.enable_spies', arez.enable_spies);

/** @define {string} */
arez.enable_native_components = goog.define('arez.enable_native_components', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine(
  'arez.enable_native_components',
  arez.enable_native_components);

/** @define {string} */
arez.enable_registries = goog.define('arez.enable_registries', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine('arez.enable_registries', arez.enable_registries);

/** @define {string} */
arez.enable_observer_error_handlers = goog.define('arez.enable_observer_error_handlers', 'true');
goog.module.get('jre').addSystemPropertyFromGoogDefine(
  'arez.enable_observer_error_handlers',
  arez.enable_observer_error_handlers);

/** @define {string} */
arez.enable_task_interceptor = goog.define('arez.enable_task_interceptor', 'true');
goog.module.get('jre').addSystemPropertyFromGoogDefine(
  'arez.enable_task_interceptor',
  arez.enable_task_interceptor);

/** @define {string} */
arez.check_invariants = goog.define('arez.check_invariants', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine('arez.check_invariants', arez.check_invariants);

/** @define {string} */
arez.check_expensive_invariants = goog.define('arez.check_expensive_invariants', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine(
  'arez.check_expensive_invariants',
  arez.check_expensive_invariants);

/** @define {string} */
arez.check_api_invariants = goog.define('arez.check_api_invariants', 'false');
goog.module.get('jre').addSystemPropertyFromGoogDefine(
  'arez.check_api_invariants',
  arez.check_api_invariants);

/** @define {string} */
arez.logger = goog.define('arez.logger', 'none');
goog.module.get('jre').addSystemPropertyFromGoogDefine('arez.logger', arez.logger);
