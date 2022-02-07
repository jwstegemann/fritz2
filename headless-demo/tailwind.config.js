// tailwind.config.js
const defaultTheme = require('../build/js/node_modules/tailwindcss/defaultTheme')

module.exports = {
    mode: 'jit', // undefined
    content: [
        './kotlin/**/*.{js,html,css}',
    ],
    darkMode: false, // or 'media' or 'class'
    theme: {
        extend: {
            colors: {
                'primary': {
                    DEFAULT: '#002EA7',
                    '50': '#D2DFFF',
                    '100': '#B1C7FF',
                    '200': '#6F97FF',
                    '300': '#2D67FF',
                    '400': '#0040E9',
                    '500': '#002EA7',
                    '600': '#00237E',
                    '700': '#001855',
                    '800': '#000C2D',
                    '900': '#000104'
                },
                'secondary': {
                    DEFAULT: '#DA291C',
                    '50': '#FBE2E0',
                    '100': '#F8CDCA',
                    '200': '#F2A29D',
                    '300': '#ED786F',
                    '400': '#E74D42',
                    '500': '#DA291C',
                    '600': '#AD2116',
                    '700': '#801810',
                    '800': '#520F0B',
                    '900': '#250705'
                },
                'tertiary': {
                    DEFAULT: '#A7C452',
                    '50': '#FEFEFD',
                    '100': '#F4F8EA',
                    '200': '#E1EBC4',
                    '300': '#CEDE9E',
                    '400': '#BAD178',
                    '500': '#A7C452',
                    '600': '#8DA93A',
                    '700': '#6D832D',
                    '800': '#4E5D20',
                    '900': '#2E3713'
                },
                'success': {
                    DEFAULT: '#58C322',
                    '50': '#DFF7D3',
                    '100': '#D0F4BD',
                    '200': '#B0EC92',
                    '300': '#91E467',
                    '400': '#71DD3B',
                    '500': '#58C322',
                    '600': '#44981A',
                    '700': '#316C13',
                    '800': '#1D410B',
                    '900': '#0A1504'
                },
                'warning': {
                    DEFAULT: '#FFAB1A',
                    '50': '#FFFFFF',
                    '100': '#FFF6E6',
                    '200': '#FFE3B3',
                    '300': '#FFD080',
                    '400': '#FFBE4D',
                    '500': '#FFAB1A',
                    '600': '#E69200',
                    '700': '#B37100',
                    '800': '#805100',
                    '900': '#4D3100'
                },
                'error': {
                    DEFAULT: '#D41111',
                    '50': '#FBCFCF',
                    '100': '#F9B8B8',
                    '200': '#F58989',
                    '300': '#F25959',
                    '400': '#EE2A2A',
                    '500': '#D41111',
                    '600': '#A50D0D',
                    '700': '#760909',
                    '800': '#460606',
                    '900': '#170202'
                },
            }
        },
    },
    variants: {},
    plugins: [
        require('../build/js/node_modules/@tailwindcss/forms')
    ],
}
