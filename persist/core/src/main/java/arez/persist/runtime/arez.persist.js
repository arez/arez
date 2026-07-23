/**
 * This file provides the @defines for ArezPersist configuration options.
 * See arez.persist.Config.java for details.
 */
goog.provide('arez.persist');
goog.require('jre');

/** @define {string} */
arez.persist.environment = goog.define('arez.persist.environment', 'production');
jre.addSystemPropertyFromGoogDefine('arez.persist.environment', arez.persist.environment);

/** @define {string} */
arez.persist.enable_application_store = goog.define('arez.persist.enable_application_store', 'true');
jre.addSystemPropertyFromGoogDefine(
  'arez.persist.enable_application_store',
  arez.persist.enable_application_store);

/** @define {string} */
arez.persist.check_api_invariants = goog.define('arez.persist.check_api_invariants', 'false');
jre.addSystemPropertyFromGoogDefine(
  'arez.persist.check_api_invariants',
  arez.persist.check_api_invariants);

/** @define {string} */
arez.persist.logger = goog.define('arez.persist.logger', 'none');
jre.addSystemPropertyFromGoogDefine('arez.persist.logger', arez.persist.logger);
