(() => {
    const cssRule = config.module.rules.find(r => "test.css".match(r.test));
    if (!cssRule) {
        throw new Error("Could not resolve webpack rule matching .css files.");
    }
    cssRule.use.push({
        loader: "postcss-loader",
        options: {
            postcssOptions: {
                plugins: [
                    require("tailwindcss")({config: "../../../../headless-demo/tailwind.config.js"}),
                    require("autoprefixer")
                ]
            }
        }
    });
})();