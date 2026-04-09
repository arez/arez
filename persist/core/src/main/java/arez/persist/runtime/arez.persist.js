/**
 * This file provides the @defines for ArezPersist configuration options.
 * See arez.persist.Config.java for details.
 */
goog.provide('arez.persist');

/** @define {string} */
arez.persist.environment = goog.define('arez.persist.environment', 'production');

/** @define {string} */
arez.persist.enable_application_store = goog.define('arez.persist.enable_application_store', 'true');

/** @define {string} */
arez.persist.check_api_invariants = goog.define('arez.persist.check_api_invariants', 'false');

/** @define {string} */
arez.persist.logger = goog.define('arez.persist.logger', 'none');
