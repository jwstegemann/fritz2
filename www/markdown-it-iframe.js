// allow iframes in markdown
// for more info see https://github.com/rjriel/markdown-it-iframe
"use strict"

module.exports = function iframe_plugin(md, options) {
    let attrs = [];
    options = options || {}

    if (options.allowfullscreen) {
        attrs.push(["allowfullscreen", true])
    }
    attrs.push(["frameborder", options.frameborder || 0])

    if (options.width) {
        attrs.push(["width", options.width])
    }
    if (options.height) {
        attrs.push(["height", options.height])
    }

    function iframe(state, startLine, endLine, silent) {
        let ch, st, token;

        let pos = state.bMarks[startLine] + state.tShift[startLine],
            max = state.eMarks[startLine];

        ch = state.src.substring(pos, pos + 1)
        if (ch !== "@" || pos + 2 >= max) {
            return false
        }

        st = state.src.substring(pos + 1, pos + 2)
        if (st !== "h" && st !== "/") {
            return false
        }

        state.line = startLine + 1

        let content = state.src.slice(pos + 1, max).trim();

        if (!silent) {
                token = state.push("div_open", "div", 1)
                token.attrs = [["class", "iframe-container"]]
                token = state.push("iframe_open", "iframe", 1)
                token.markup = "@"
                token.attrs = attrs.concat([["src", content]])
                token.map = [startLine, state.line]

                token = state.push("iframe_close", "iframe", -1)
                token.markup = "@"
                token = state.push("div_close", "div", -1)
        }
        return true
    }

    md.block.ruler.before("paragraph", "iframe", iframe)
}