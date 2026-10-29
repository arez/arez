goog.module('arez.MappedRelationshipTest');

const RelationshipTest = goog.require('arez.browser.MappedRelationshipBrowserTest');
const testSuite = goog.require('goog.testing.testSuite');

testSuite({
  testMappedTargetIdentityPresentationAndLifecycle() {
    RelationshipTest.exerciseRelationship();
  },
});
