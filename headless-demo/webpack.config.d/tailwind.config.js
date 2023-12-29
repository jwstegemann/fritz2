const defaultTheme = require('tailwindcss/defaultTheme')

// must be in the jsMain/resource folder
const mainCssFile = 'styles.css';

const tailwind = {
    darkMode: 'media',
    plugins: [
        require('@tailwindcss/forms')
    ],
    variants: {},
    theme: {
        fontFamily: {
            sans: ['Inter var', ...defaultTheme.fontFamily.sans],
        },
        extend: {
            backgroundImage: {
                'radial-at-b':
                    'radial-gradient(ellipse at bottom, var(--tw-gradient-stops))',
            },
            colors: {
                'primary': {
                    DEFAULT: '#6D97AB',
                    '50': '#FBFDFD',
                    '100': '#EEF6F5',
                    '200': '#D5E7E6',
                    '300': '#BBD6D8',
                    '400': '#A1C3C9',
                    '500': '#87AEBA',
                    '600': '#6D97AB',
                    '700': '#50738B',
                    '800': '#3A4F64',
                    '900': '#232E3D'
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
    content: {
        files: [
            '*.{js,html,css}',
            './kotlin/**/*.{js,html,css}'
        ],
        transform: {
            js: (content) => {
                return content.replaceAll(/(\\r)|(\\n)|(\\r\\n)/g, ' ')
            }
        }
    },
};


// webpack tailwind css settings
((config) => {
    let entry = '/kotlin/' + mainCssFile;
    config.entry.main.push(entry);
    config.module.rules.push({
        test: /\.css$/,
        use: [
            {loader: 'style-loader'},
            {loader: 'css-loader'},
            {
                loader: 'postcss-loader',
                options: {
                    postcssOptions: {
                        plugins: [
                            require("tailwindcss")({config: tailwind}),
                            require("autoprefixer"),
                            require("cssnano")
                        ]
                    }
                }
            }
        ]
    });
})(config);