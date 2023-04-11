/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./App.{js,jsx,ts,tsx}", "./Components/**/*.{js,jsx,ts,tsx}", "./Screens/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        'color': {
          'white': '#ffffff',
          'greyLight': '#616161',
          'greyDark': '#2D2D2D',
          'accent': '#2CB039',
          'accentInactive' :'#66c970',
          'warning': '#ff7a7a'

        },
      },
    },
  },
  plugins: [],
}
