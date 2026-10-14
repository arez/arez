goog.module('arez.dom.example.DocumentVisibilityExampleTest');
const BrowserAssertions = goog.require('arez.browser.BrowserAssertions');
const Example = goog.require('arez.dom.example.DocumentVisibilityExample');
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
    root = Fixtures.append('<div id="visibility"></div>');
    restore = Fixtures.replaceProperty(document, 'visibilityState', 'visible');
    events = Fixtures.trackEvents(document, 'visibilitychange');
    app = Example.bootstrap();
    BrowserAssertions.expectText('visibility', 'Document Visibility: VISIBLE');
    asserts.assertEquals(1, events.count());
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
  testVisibilityChanges() {
    Reflect.set(document, 'visibilityState', 'hidden');
    document.dispatchEvent(new Event('visibilitychange'));
    BrowserAssertions.expectText('visibility', 'Document Visibility: HIDDEN');
    Reflect.set(document, 'visibilityState', 'prerender');
    document.dispatchEvent(new Event('visibilitychange'));
    BrowserAssertions.expectText('visibility', 'Document Visibility: PRERENDER');
  },
  testDisposal() {
    app.dispose();
    asserts.assertEquals(0, events.count());
    Reflect.set(document, 'visibilityState', 'hidden');
    document.dispatchEvent(new Event('visibilitychange'));
    BrowserAssertions.expectText('visibility', 'Document Visibility: VISIBLE');
  },
});
