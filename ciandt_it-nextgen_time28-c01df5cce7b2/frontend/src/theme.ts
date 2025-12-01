// theme.ts
'use client';
import { createTheme } from '@mui/material/styles';

declare module '@mui/material/styles' {
  interface PaletteOptions {
    texts?: {
      darkPurple?: string
      gray?: string

    };
    borders?: {
      white?: string
      almost_white?: string
    };
    buttons?: {
      blue?: string;
      pink?: string;
      darkPink?: string;
      light?: string;
      red?: string;
      lightBlue?: string;
    }
  }
  
  interface Palette {
    texts: {
      darkPurple: string;
      gray: string;
    }
    borders: {
      white: string;
      almost_white: string
    }
    buttons: {
      blue: string;
      pink: string;
      darkPink: string;
      light: string;
      red: string;
      lightBlue: string;
    }
  }
}

const theme = createTheme({
  typography: {
    allVariants: {
      color: '#000000',
    },
    fontFamily: 'var(--font-dm-sans)',
    subtitle1: {
      fontWeight: 700,
      fontSize: '16px',
      lineHeight: '100%',
      letterSpacing: '0%',
    },
    subtitle2: {
      fontWeight: 700,
      fontSize: '18px',
      lineHeight: '100%',
      letterSpacing: '0%',
      color: '#020050',
    },
    body1: {
      fontWeight: 500,
      fontSize: '16px',
      lineHeight: '100%',
      letterSpacing: '0%',
    },
    body2: {
      fontSize: '16px',
    },
  },
  palette: {
    divider: '#f0f0f0',

    primary: {
      main: '#A34A87', //Purple
    },
    secondary: {
      main: '#484794', //Blue
    },
    error: {
      main: '#fa5a50', //Red
      dark: '#e54d43', //Dark Red (for hover)
    },
    background: {
      default: '#f5f5f5', //Light 
      paper: '#ffffff',
    },
    text: {
      primary: '#000000',  //Black
      secondary: '#ffffff',  //White
    },
    texts:{
      darkPurple: '#020050',
      gray: '#8C8C8C',
    },
    borders:{
      white: '#ffffff',
      almost_white: '#f0f0f0'
    },
    buttons:{
      blue: '#484794',
      pink: '#9B4274',
      darkPink: '#7a335a',
      red: '#EF4444',
      lightBlue: '#F1F4FF',
    }
  },
});

export default theme;
