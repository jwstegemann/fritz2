config.set({
    // disables web-security feature in Chrome headless
    // to prevent that Chrome checks CORS-policy of the "test-server"
    browsers: ['ChromeHeadlessNoSecurity'],
    customLaunchers: {
        ChromeHeadlessNoSecurity: {
            base: 'ChromeHeadless',
            flags: ['--disable-web-security']
        }
    },
    client: {
        mocha: {
            timeout: "15s"
        }
    }
});