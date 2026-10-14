import hljs from './highlight/core.min.js';
import java from './highlight/java.min.js';
import xml from './highlight/xml.min.js';

hljs.registerLanguage('java', java);
hljs.registerLanguage('xml', xml);
for (const code of document.querySelectorAll('pre code.language-java, pre code.language-xml')) {
  hljs.highlightElement(code);
}
