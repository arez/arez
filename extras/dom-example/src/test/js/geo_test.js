goog.module('arez.dom.example.GeoPositionExampleTest');
const BrowserAssertions = goog.require('arez.browser.BrowserAssertions');
const Example = goog.require('arez.dom.example.GeoPositionExample');
const Fixtures = goog.require('arez.browser.Fixtures');
const Mode = goog.require('arez.browser.Mode');
const asserts = goog.require('goog.testing.asserts');
const testSuite = goog.require('goog.testing.testSuite');

/** @type {!Example} */
let app;
/** @type {!Element} */
let root;
/** @type {function():void} */
let restore;
/** @type {function(!GeolocationPosition):void} */
let success;
/** @type {function(!GeolocationPositionError):void} */
let failure;
let watches = 0;
let cleared = 0;

testSuite({
  setUp() {
    watches = 0;
    cleared = 0;
    /**
     * @param {function(!GeolocationPosition):void} onSuccess
     * @param {function(!GeolocationPositionError):void} onFailure
     * @return {number}
     */
    const watchPosition = (onSuccess, onFailure) => {
      success = onSuccess;
      failure = onFailure;
      watches++;
      return 42;
    };
    /** @param {number} id */
    const clearWatch = (id) => {
      asserts.assertEquals(42, id);
      cleared++;
    };
    restore = Fixtures.replaceProperty(navigator, 'geolocation', {
      'watchPosition': watchPosition,
      'clearWatch': clearWatch,
    });
    root = Fixtures.append('<button id="watch"></button><div id="status"></div>');
    app = Example.bootstrap();
    BrowserAssertions.expectText('status', 'GeoPosition: -2');
    asserts.assertEquals(0, watches);
    BrowserAssertions.expectChecks('development' === Mode.environment);
  },
  tearDown() {
    app.dispose();
    asserts.assertTrue(app.isDisposed());
    asserts.assertEquals(watches, cleared);
    restore();
    root.remove();
  },
  testPositionAndErrorRecovery() {
    Fixtures.click('watch');
    BrowserAssertions.expectText('status', 'GeoPosition: -1');
    asserts.assertEquals(1, watches);
    success(/** @type {!GeolocationPosition} */ ({
      'coords': {'accuracy': 10, 'altitude': null, 'heading': null, 'latitude': 1.25, 'longitude': 2.5, 'speed': null},
    }));
    BrowserAssertions.expectText('status', 'GeoPosition: 0 Position: 1.25, 2.5');
    failure(/** @type {!GeolocationPositionError} */ ({'code': 1, 'message': 'permission denied'}));
    BrowserAssertions.expectText('status', 'GeoPosition: 1 ErrorMessage: permission denied');
    failure(/** @type {!GeolocationPositionError} */ ({'code': 3, 'message': 'timeout'}));
    BrowserAssertions.expectText('status', 'GeoPosition: 3 ErrorMessage: timeout');
    success(/** @type {!GeolocationPosition} */ ({
      'coords': {'accuracy': 10, 'altitude': null, 'heading': null, 'latitude': 3, 'longitude': 4, 'speed': null},
    }));
    BrowserAssertions.expectText('status', 'GeoPosition: 0 Position: 3, 4');
  },
  testWatchAndDisposal() {
    Fixtures.click('watch');
    Fixtures.click('watch');
    asserts.assertEquals(1, watches);
    app.dispose();
    asserts.assertEquals(1, cleared);
    Fixtures.click('watch');
    asserts.assertEquals(1, watches);
  },
});
