goog.module('arez.persist.WebStorageServiceTest');

const StorageTest = goog.require('arez.persist.runtime.browser.WebStorageServiceBrowserTest');
const asserts = goog.require('goog.testing.asserts');
const testSuite = goog.require('goog.testing.testSuite');

testSuite({
  testRoundTripPreservesNestedStateAndNullValues() {
    asserts.assertTrue(StorageTest.roundTrip());
  },

  testDecodeIgnoresInheritedProperties() {
    const encoded = Object.create({inherited: 'ignored'});
    encoded.own = 'retained';
    asserts.assertEquals(1, StorageTest.decodedPropertyCount(encoded));
  },
});
