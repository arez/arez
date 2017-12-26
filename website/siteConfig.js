const RemarkableEmbed = require('remarkable-embed');
const fs = require('fs');

const markdownInclude = function(code) {
  const filename = process.cwd() + '/includes/' + code;
  return fs.readFileSync(filename, 'utf8');
};

const apiUrl = function(code) {
  const elements = code.split('::');

  const label =
    elements.length >
    1 ?
    elements[0] :
    ((elements[0].match(/^annotations\./) ? '@' : '') + elements[0].replace(/^.+\./, '') );
  const classname = elements.length > 1 ? elements[1] : elements[0];
  const url = '/api/org/realityforge/arez/' +
              classname.replace('.', '/') + '.html' +
              (elements.length > 2 ? '#' + elements[2].replace('(', '-').replace(')', '-') : '');

  return `<a href="${url}"><code>${label}</code></a>`;
};

function parseParams(params) {
  params = params.trim();
  let args = {};
  let paramsLeft = params;
  while (paramsLeft) {
    const result = PARAM_EXTRACTOR.exec(paramsLeft);
    if (result) {
      const param = result[2] || result[3] || result[4];
      const eqIndex = param.indexOf('=');
      if (-1 === eqIndex) {
        throw Error(`Bad parameter ${param} extracted from ${params}`);
      }
      const key = param.slice(0, eqIndex);
      args[key] = param.slice(eqIndex + 1);
      paramsLeft = paramsLeft.slice(result[0].length);
    }
    else {
      break;
    }
  }
  return args;
}

const PARAM_EXTRACTOR = /(([^\s"'][^\s\}]*)|"([^\"]*)"|'([^']*)')\s*/;

function calculateFirstLine(lines, pattern, includeLine) {
  if (!pattern) {
    return 0;
  }

  const regex = new RegExp(pattern);
  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    if (regex.test(line)) {
      return i + (includeLine ? 0 : 1);
    }
  }
  throw Error(`Unable to locate first line with pattern ${pattern}`);
}

function calculateLastLine(lines, startLine, pattern, includeLine) {
  if (!pattern) {
    return lines.length - 1;
  }

  const regex = new RegExp(pattern);
  for (let i = startLine; i < lines.length; i++) {
    const line = lines[i];
    if (regex.test(line)) {
      return i + (includeLine ? 1 : 0);
    }
  }
  throw Error(`Unable to locate last line with pattern ${pattern}`);
}

const fileContent = function(params, options) {
  let args = parseParams(params);

  const project = args['project'] || 'doc-examples';
  const path = args['path'] || 'src/main/java';
  const file = args['file'];
  const language = args['language'] || 'java';
  const firstLine = args['start_line'];
  const lastLine = args['end_line'];
  const includeStartLine = (args['include_start_line'] || 'true') === 'true';
  const includeEndLine = (args['include_end_line'] || 'true') === 'true';
  const stripBlock = (args['strip_block'] || 'false') === 'true';
  const elideStart = args['elide_start'] || 'DOC ELIDE START';
  const elideEnd = args['elide_end'] || 'DOC ELIDE END';
  const elideReplacement = args['elide_replacement'] || '...';

  if (!file) {
    throw Error(`Failed to specify file parameter ${file}`);
  }

  const filename = process.cwd() + '/../' + project + '/' + path + '/' + file;
  const content = fs.readFileSync(filename, 'utf8');
  const lines = content.split('\n');

  const start = calculateFirstLine(lines, firstLine, includeStartLine);
  const end = calculateLastLine(lines, start, lastLine, includeEndLine);

  const selectedLines = lines.slice(start, end);

  const elideStartRegex = new RegExp(elideStart);
  const elideEndRegex = new RegExp(elideEnd);

  let whitespaceAtStart = 0 === selectedLines.length ? 10000000 : lines[0].length;
  let lastWhitespaceAtStart = 0;
  let inElission = false;

  const newSelectedLines = [];
  for (let i = 0; i < selectedLines.length; i++) {
    const line = selectedLines[i];
    if (!inElission && elideStartRegex.test(line)) {
      inElission = true;
      newSelectedLines.push(Array(lastWhitespaceAtStart + 1).join(' ') + elideReplacement);
    }
    else if (inElission && elideEndRegex.test(line)) {
      inElission = false;
    }
    else if (!inElission) {
      if (0 !== line.length) {
        lastWhitespaceAtStart = line.search(/\S|$/);
        whitespaceAtStart = Math.min(whitespaceAtStart, lastWhitespaceAtStart);
      }
      newSelectedLines.push(line);
    }
  }
  if (stripBlock) {
    for (let i = 0; i < newSelectedLines.length; i++) {
      newSelectedLines[i] = newSelectedLines[i].slice(whitespaceAtStart);
    }
  }
  const newContent = newSelectedLines.join('\n');

  return '<pre><code>' + options.highlight(newContent, language) + '</code></pre>';
};

const embed = new RemarkableEmbed.Plugin();
embed.reg = /{@(\w+)\s*:\s*((([^\s"'][^\s\}]*|"[^\"]*"|'[^']*')\s*)+?)}/;
embed.register({
  youtube: RemarkableEmbed.extensions.youtube,
  file_content: fileContent,
  api_url: apiUrl,
  include: markdownInclude
});

const siteConfig = {
  title: 'Arez',
  tagline: 'Fast, easy, reactive state',
  url: 'https://arez.github.io',
  baseUrl: '/',
  projectName: 'arez',
  headerLinks: [
    { doc: 'overview', label: 'Docs' },
    { href: '/api', label: 'API' },
    { page: 'help', label: 'Help' },
    { blog: true, label: 'Blog' },
    { href: 'https://github.com/arez/arez', label: 'GitHub', external: true }
  ],
  users: [],
  /* path to images for header/footer */
  headerIcon: 'img/logo.svg',
  footerIcon: 'img/logo.svg',
  favicon: 'img/favicon-32x32.png',
  /* colors for website */
  colors: {
    /* This is derived from https://coolors.co/4464ad-393939-a4b0f5-4481d6-f9f9f9 */
    primaryColor: '#4481D6',
    secondaryColor: '#4464AD'
  },
  // This copyright info is used in /core/Footer.js and blog rss/atom feeds.
  copyright: 'Copyright © ' + new Date().getFullYear() + ' the Arez Project',
  organizationName: 'arez',
  highlight: {
    // Highlight.js theme to use for syntax highlighting in code blocks
    theme: 'idea'
  },
  scripts: ['https://buttons.github.io/buttons.js'],
  repoUrl: 'https://github.com/arez/arez',
  editUrl: 'https://github.com/arez/arez/tree/master/docusaurus/docs',
  markdownPlugins: [
    embed.hook
  ]
};

module.exports = siteConfig;
