goog.module('arez.dom.example.NetworkStatusExampleTest');
const BrowserAssertions = goog.require('arez.browser.BrowserAssertions');
const Example = goog.require('arez.dom.example.NetworkStatusExample');
const Fixtures = goog.require('arez.browser.Fixtures');
const Mode = goog.require('arez.browser.Mode');
const asserts = goog.require('goog.testing.asserts');
const testSuite = goog.require('goog.testing.testSuite');

/** @type {!Example} */
let app;
/** @type {!Element} */
let root;
/** @type {{count:function():number, restore:function():void}} */
let events;
/** @type {function():void} */
let restore;

testSuite({
  setUp() {
    root = Fixtures.append('<div id="network"></div>');
    restore = Fixtures.replaceProperty(navigator, 'onLine', true);
    events = Fixtures.trackEvents(window, ['online', 'offline']);
    app = Example.bootstrap();
    BrowserAssertions.expectText('network', 'Network Status: Online');
    asserts.assertEquals(2, events.count());
    BrowserAssertions.expectChecks('development' === Mode.environment);
  },
  tearDown() {
    app.dispose();
    asserts.assertTrue(app.isDisposed());
    asserts.assertEquals(0, events.count());
    events.restore();
    restore();
    root.remove();
  },
  testNetworkChanges() {
    Reflect.set(navigator, 'onLine', false);
    window.dispatchEvent(new Event('offline'));
    BrowserAssertions.expectText('network', 'Network Status: Offline');
    Reflect.set(navigator, 'onLine', true);
    window.dispatchEvent(new Event('online'));
    BrowserAssertions.expectText('network', 'Network Status: Online');
  },
  testDisposal() {
    app.dispose();
    asserts.assertEquals(0, events.count());
    Reflect.set(navigator, 'onLine', false);
    window.dispatchEvent(new Event('offline'));
    BrowserAssertions.expectText('network', 'Network Status: Online');
  },
});
