goog.module('arez.dom.example.WindowSizeExampleTest');
const BrowserAssertions = goog.require('arez.browser.BrowserAssertions');
const Example = goog.require('arez.dom.example.WindowSizeExample');
const Fixtures = goog.require('arez.browser.Fixtures');
const Mode = goog.require('arez.browser.Mode');
const asserts = goog.require('goog.testing.asserts');
const testSuite = goog.require('goog.testing.testSuite');

const originalWidth = Object.getOwnPropertyDescriptor(window, 'innerWidth');
const originalHeight = Object.getOwnPropertyDescriptor(window, 'innerHeight');
/** @type {!Example} */
let app;
/** @type {!Element} */
let root;
/** @type {{count: function():number, restore: function():void}} */
let events;

/**
 * @param {number} width
 * @param {number} height
 */
function size(width, height) {
  Object.defineProperty(window, 'innerWidth', {configurable: true, value: width});
  Object.defineProperty(window, 'innerHeight', {configurable: true, value: height});
  window.dispatchEvent(new Event('resize'));
}

testSuite({
  setUp() {
    root = Fixtures.append('<div id="status"></div>');
    size(800, 600);
    events = Fixtures.trackEvents(window, 'resize');
    app = Example.bootstrap();
    BrowserAssertions.expectText('status', 'Screen size: 800 x 600');
    asserts.assertEquals(1, events.count());
    BrowserAssertions.expectChecks('development' === Mode.environment);
  },
  tearDown() {
    app.dispose();
    asserts.assertTrue(app.isDisposed());
    asserts.assertEquals(0, events.count());
    events.restore();
    root.remove();
    if (originalWidth) Object.defineProperty(window, 'innerWidth', originalWidth);
    else delete window.innerWidth;
    if (originalHeight) Object.defineProperty(window, 'innerHeight', originalHeight);
    else delete window.innerHeight;
  },
  testResize() {
    size(1024, 768);
    BrowserAssertions.expectText('status', 'Screen size: 1024 x 768');
  },
  testDisposal() {
    app.dispose();
    asserts.assertEquals(0, events.count());
    size(1200, 900);
    BrowserAssertions.expectText('status', 'Screen size: 800 x 600');
  },
});
