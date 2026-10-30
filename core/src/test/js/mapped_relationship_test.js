goog.module('arez.MappedRelationshipTest');

const RelationshipTest = goog.require('arez.browser.MappedRelationshipBrowserTest');
const testSuite = goog.require('goog.testing.testSuite');

testSuite({
  testRuntimeTypedTargetsAndInvalidFactoryResults() {
    RelationshipTest.exerciseRuntimeTypes();
  },
  testMappedTargetIdentityPresentationAndLifecycle() {
    RelationshipTest.exerciseRelationship();
  },
});
