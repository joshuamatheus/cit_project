import React from 'react';
import Typography from '@mui/material/Typography';
import theme from '@/theme';

const LoginPageTitle = ({ text }: { text: string }) => (
  <Typography 
    variant="h4" 
    component="h1" 
    sx={{ 
      marginBottom: '4px',
      textAlign: 'center',
      color: theme.palette.text.secondary
    }}
  >
    {text}
  </Typography>
);

export default LoginPageTitle;
