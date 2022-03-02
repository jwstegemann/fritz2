const colors = require('tailwindcss/colors')

module.exports = {
  content: [
    "./src/_includes/**/*.{html,md,11ty.js,liquid,njk,hbs,mustache,ejs,haml,pug}",
    "./src/blog/**/*.{html,md,11ty.js,liquid,njk,hbs,mustache,ejs,haml,pug}",
    "./src/pages/**/*.{html,md,11ty.js,liquid,njk,hbs,mustache,ejs,haml,pug}",
    "./src/index.{html,md,11ty.js,liquid,njk,hbs,mustache,ejs,haml,pug}",
  ],
  theme: {
    extend: {
      maxWidth: {
        'prose': '100ch',
      },
      fontFamily: {
        'nunito': ['Nunito', 'sans-serif'],
        'sans': ['Inter', 'sans-serif']
      },
      colors: {
        bg: {
          DEFAULT: colors.gray["900"],
          highlight: colors.gray["800"],
          focus: colors.gray["400"],
        },
        fg: {
          DEFAULT: colors.cyan["700"],
          highlight: colors.cyan["200"],
          focus: colors.cyan["300"],
        },
        relaxed: {
          DEFAULT: colors.gray["400"],
          highlight: colors.gray["50"],
        },
      }
    }
  },
  plugins: [
    require('@tailwindcss/typography')
  ]
}
