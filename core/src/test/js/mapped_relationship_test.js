goog.module('arez.MappedRelationshipTest');

const RelationshipTest = goog.require('arez.browser.MappedRelationshipBrowserTest');
const testSuite = goog.require('goog.testing.testSuite');

testSuite({
  testContextualMappedTargetIdentityAndLifecycle() {
    RelationshipTest.exerciseContextualRelationship();
  },
  testValueKeyMatchingAndLifecycle() {
    RelationshipTest.exerciseValueKeys();
  },
  testRuntimeTypedTargetsAndInvalidFactoryResults() {
    RelationshipTest.exerciseRuntimeTypes();
  },
  testMappedTargetIdentityPresentationAndLifecycle() {
    RelationshipTest.exerciseRelationship();
  },
});
