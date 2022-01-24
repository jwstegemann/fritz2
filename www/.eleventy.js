const eleventyNavigationPlugin = require("@11ty/eleventy-navigation");

const markdownIt = require('markdown-it')
const markdownItAnchor = require('markdown-it-anchor')
const pluginTOC = require('eleventy-plugin-toc')

module.exports = function(eleventyConfig) {

  eleventyConfig.addPlugin(eleventyNavigationPlugin);

  eleventyConfig.addPassthroughCopy('src/img')
  eleventyConfig.addPassthroughCopy('admin')

  eleventyConfig.setBrowserSyncConfig({
    port: 9090,
    serveStatic: ['../headless-demo/']
  });

  const {
    DateTime
  } = require("luxon");

  // https://html.spec.whatwg.org/multipage/common-microsyntaxes.html#valid-date-string
  eleventyConfig.addFilter('htmlDateString', (dateObj) => {
    return DateTime.fromJSDate(dateObj, {
      zone: 'utc'
    }).toFormat('yy-MM-dd');
    });

    eleventyConfig.addFilter("readableDate", dateObj => {
    return DateTime.fromJSDate(dateObj, {
      zone: 'utc'
    }).toFormat("dd-MM-yy");
  });

  // Markdown
  eleventyConfig.setLibrary(
      'md',
      markdownIt().use(markdownItAnchor)
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
