goog.module('arez.dom.example.IdleStatusExampleTest');
const BrowserAssertions = goog.require('arez.browser.BrowserAssertions');
const Clock = goog.require('arez.browser.Clock');
const Example = goog.require('arez.dom.example.IdleStatusExample');
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
/** @type {{advance:function(number):void, pending:function():number, restore:function():void}} */
let clock;

testSuite({
  setUp() {
    root = Fixtures.append('<div id="idle"></div>');
    clock = Clock.install();
    events = Fixtures.trackEvents(window, ['keydown', 'touchstart', 'scroll', 'mousemove', 'mouseup', 'mousedown', 'wheel']);
    app = Example.bootstrap();
    BrowserAssertions.expectText('idle', 'Interaction Status: Active');
    asserts.assertEquals(7, events.count());
    asserts.assertEquals(1, clock.pending());
    BrowserAssertions.expectChecks('development' === Mode.environment);
  },
  tearDown() {
    app.dispose();
    asserts.assertTrue(app.isDisposed());
    asserts.assertEquals(0, events.count());
    asserts.assertEquals(0, clock.pending());
    events.restore();
    clock.restore();
    root.remove();
  },
  testIdleAndActivity() {
    clock.advance(1999);
    BrowserAssertions.expectText('idle', 'Interaction Status: Active');
    clock.advance(1);
    BrowserAssertions.expectText('idle', 'Interaction Status: Idle');
    window.dispatchEvent(new Event('mousemove'));
    BrowserAssertions.expectText('idle', 'Interaction Status: Active');
    clock.advance(2000);
    BrowserAssertions.expectText('idle', 'Interaction Status: Idle');
  },
  testDisposal() {
    app.dispose();
    asserts.assertEquals(0, events.count());
    asserts.assertEquals(0, clock.pending());
    clock.advance(3000);
    window.dispatchEvent(new Event('mousemove'));
    BrowserAssertions.expectText('idle', 'Interaction Status: Active');
  },
});
