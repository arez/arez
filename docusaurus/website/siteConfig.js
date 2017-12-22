const RemarkableEmbed = require('remarkable-embed');
const fs = require('fs');

const markdownInclude = function(code) {
  const filename = process.cwd() + '/includes/' + code;
  return fs.readFileSync(filename, 'utf8');
};

const apiUrl = function(code) {
  const elements = code.split('::');

  const label = elements.length > 1 ? elements[0] : ((elements[0].match(/^annotations\./) ? '@' : '') + elements[0].replace(/^.+\./, '') );
  const classname = elements.length > 1 ? elements[1] : elements[0];
  const url = '/api/org/realityforge/arez/' +
              classname.replace('.', '/') + '.html' +
              (elements.length > 2 ? '#' + elements[2].replace('(', '-').replace(')', '-') : '');

  return `<a href="${url}"><code>${label}</code></a>`;
};

const embed = new RemarkableEmbed.Plugin();
embed.register({
  youtube: RemarkableEmbed.extensions.youtube,
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
  markdownPlugins: [
    embed.hook
  ]
};

module.exports = siteConfig;
