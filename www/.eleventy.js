const path = require('path');
const fs = require('fs');

const markdownIt = require('markdown-it');
const markdownItKbd = require('markdown-it-kbd');
const markdownItAnchor = require('markdown-it-anchor');
const markdownItContainer = require('markdown-it-container');
const markdownItImsize = require('markdown-it-imsize');

const tocPlugin = require('eleventy-plugin-toc');
const heroiconsPlugin = require('eleventy-plugin-heroicons');
const syntaxhighlightPlugin = require("@11ty/eleventy-plugin-syntaxhighlight");
const navigationPlugin = require("@11ty/eleventy-navigation");

module.exports = (config) => {

    config.addPlugin(navigationPlugin);
    config.addPlugin(heroiconsPlugin, {
        errorOnMissing: true
    });
    config.addPlugin(syntaxhighlightPlugin);

    config.addPassthroughCopy('src/assets')
    config.addPassthroughCopy('src/img')
    config.addPassthroughCopy({'src/icon': '.'})

    config.setBrowserSyncConfig({
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
            {
                route: '/api',
                dir: '../api'
            },
            // example pages
            ...fs.readdirSync(path.join(__dirname, '../examples'), { withFileTypes: true })
                .filter(entry => entry.isDirectory())
                .map(dir => (
                    {
                        route: '/examples/' + dir.name,
                        dir: path.join('../examples/', dir.name, '/build/distributions')
                    }
                ))
        ]
    });

    const {
        DateTime
    } = require("luxon");

    // https://html.spec.whatwg.org/multipage/common-microsyntaxes.html#valid-date-string
    config.addFilter('htmlDateString', (dateObj) => {
        return DateTime.fromJSDate(dateObj, {
            zone: 'utc'
        }).toFormat('yyyy-MM-dd');
    });

    config.addFilter("readableDate", dateObj => {
        return DateTime.fromJSDate(dateObj, {
            zone: 'utc'
        }).toFormat("dd-MM-yyyy");
    });

    // Markdown
    config.setLibrary(
        'md',
        markdownIt()
            .use(markdownItAnchor)
            .use(markdownItKbd)
            .use(markdownItContainer, "info")
            .use(markdownItContainer, "warning")
            .use(markdownItImsize)
    )

    config.addPlugin(tocPlugin, {
        tags: ['h2', 'h3', 'h4'], // which heading tags are selected headings must each have an ID attribute
        wrapper: 'nav',           // element to put around the root `ol`/`ul`
        wrapperClass: 'toc',      // class for the element around the root `ol`/`ul`
        ul: true,                // if to use `ul` instead of `ol`
        flat: false,
    })

    return {
        dir: {input: 'src', output: '_site'}
    };
};
