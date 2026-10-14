BrowserAssertions.text = function(id) {
  const element = document.getElementById(id);
  return element ? element.textContent : null;
};
