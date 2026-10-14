goog.module('arez.dom.example.BrowserLocationExampleTest');
const BrowserAssertions = goog.require('arez.browser.BrowserAssertions');
const Example = goog.require('arez.dom.example.BrowserLocationExample');
const Fixtures = goog.require('arez.browser.Fixtures');
const Mode = goog.require('arez.browser.Mode');
const asserts = goog.require('goog.testing.asserts');
const testSuite = goog.require('goog.testing.testSuite');

const originalUrl = location.href;
/** @type {!Example} */
let app;
/** @type {!Element} */
let root;
/** @type {{count: function():number, restore: function():void}} */
let events;

/** @param {string} hash */
function browserHash(hash) {
  history.replaceState({}, '', originalUrl.split('#')[0] + hash);
  window.dispatchEvent(new HashChangeEvent('hashchange'));
}

/** @param {string} route */
function expectRoute(route) {
  BrowserAssertions.expectText('browser_location', 'Browser Location: ' + route);
  BrowserAssertions.expectText('app_location', 'Application Location: ' + route);
}

testSuite({
  setUp() {
    history.replaceState({}, '', originalUrl.split('#')[0]);
    root = Fixtures.append('<div id="browser_location"></div><div id="app_location"></div>' +
        '<button id="route_base"></button><button id="route_slash"></button>' +
        '<button id="route_event"></button><button id="route_other"></button>');
    events = Fixtures.trackEvents(window, 'hashchange');
    app = Example.bootstrap();
    expectRoute('');
    asserts.assertEquals(1, events.count());
    BrowserAssertions.expectChecks('development' === Mode.environment);
  },
  tearDown() {
    app.dispose();
    asserts.assertTrue(app.isDisposed());
    asserts.assertEquals(0, events.count());
    events.restore();
    root.remove();
    history.replaceState({}, '', originalUrl);
  },
  testInitialEmptyHash() {
    expectRoute('');
  },
  testRouteButtons() {
    /** @type {!Array<!Array<string>>} */
    const routes = [['route_event', '/event'], ['route_other', '/other'], ['route_slash', '/'], ['route_base', '']];
    for (const [id, route] of routes) {
      Fixtures.click(id);
      expectRoute(route);
    }
  },
  testBrowserHashAndInvalidRoute() {
    browserHash('#/event');
    expectRoute('/event');
    browserHash('#invalid');
    expectRoute('/event');
    asserts.assertEquals('#/event', location.hash);
  },
  testDisposal() {
    app.dispose();
    asserts.assertEquals(0, events.count());
    Fixtures.click('route_other');
    expectRoute('');
    asserts.assertEquals('', location.hash);
  },
});
