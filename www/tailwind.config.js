const colors = require('tailwindcss/colors')

module.exports = {
  content: [
    "./src/_includes/**/*.{html,md,11ty.js,liquid,njk,hbs,mustache,ejs,haml,pug}",
    "./src/blog/**/*.{html,md,11ty.js,liquid,njk,hbs,mustache,ejs,haml,pug}",
    "./src/pages/**/*.{html,md,11ty.js,liquid,njk,hbs,mustache,ejs,haml,pug}",
    "./src/*.{html,md,11ty.js,liquid,njk,hbs,mustache,ejs,haml,pug}",
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
          end:'#87AEBA', // primary[500]
          start:'#50738B', // primary[700]
        },
        fg: {
          DEFAULT: '#50738B', // primary[700]
          highlight: '#D5E7E6', // primary[200]
          focus: '#BBD6D8', // primary[300],
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
