const eleventyNavigationPlugin = require("@11ty/eleventy-navigation");
const path = require('path');
const { readdirSync: readDir } = require('fs');

const markdownIt = require('markdown-it');
const markdownItKbd = require('markdown-it-kbd');
const markdownItAnchor = require('markdown-it-anchor');
const markdownItContainer = require('markdown-it-container');
const markdownItImsize = require('markdown-it-imsize');
const pluginTOC = require('eleventy-plugin-toc');
const syntaxHighlight = require("@11ty/eleventy-plugin-syntaxhighlight");
const heroicons = require('eleventy-plugin-heroicons');

module.exports = function(eleventyConfig) {

  eleventyConfig.addPlugin(eleventyNavigationPlugin);
  eleventyConfig.addPlugin(heroicons, {
    errorOnMissing: true
  });
  eleventyConfig.addPlugin(syntaxHighlight);

  eleventyConfig.addPassthroughCopy('src/assets')
  eleventyConfig.addPassthroughCopy('src/img')
  eleventyConfig.addPassthroughCopy({'src/icon': '.'})

  eleventyConfig.setBrowserSyncConfig({
    port: 9090,
    serveStatic: [
      {
        route: '/headless-demo',
        dir: '../headless-demo/build/distributions'
      },
      {
        route: '/examples-demo',
        dir: '../examples-demo/build/distributions'
      },
      ...readDir(path.join(path.resolve(__dirname, '..'), 'examples'), { withFileTypes: true })
        .filter(entry => entry.isDirectory())
          .map(dir => dir.name)
          .map(dirName => ({
          route: `/examples/${dirName}`,
          dir: path.join('../examples/', dirName, '/build/distributions')
      })),
      {
        route: '/api',
        dir: '../api'
      }]
  });

  const {
    DateTime
  } = require("luxon");

  // https://html.spec.whatwg.org/multipage/common-microsyntaxes.html#valid-date-string
  eleventyConfig.addFilter('htmlDateString', (dateObj) => {
    return DateTime.fromJSDate(dateObj, {
      zone: 'utc'
    }).toFormat('yyyy-MM-dd');
    });

    eleventyConfig.addFilter("readableDate", dateObj => {
    return DateTime.fromJSDate(dateObj, {
      zone: 'utc'
    }).toFormat("dd-MM-yyyy");
  });

  // Markdown
  eleventyConfig.setLibrary(
      'md',
      markdownIt()
          .use(markdownItAnchor)
          .use(markdownItKbd)
          .use(markdownItContainer, "info")
          .use(markdownItContainer, "warning")
          .use(markdownItImsize)
  )

  eleventyConfig.addPlugin(pluginTOC, {
    tags: ['h2', 'h3', 'h4'], // which heading tags are selected headings must each have an ID attribute
    wrapper: 'nav',           // element to put around the root `ol`/`ul`
    wrapperClass: 'toc',      // class for the element around the root `ol`/`ul`
    ul: true,                // if to use `ul` instead of `ol`
    flat: false,
  })

  return {
    dir: { input: 'src', output: '_site' }
  };
};
