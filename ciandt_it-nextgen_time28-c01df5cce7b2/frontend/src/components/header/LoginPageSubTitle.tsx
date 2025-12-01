import React from 'react';
import Typography from '@mui/material/Typography';
import theme from '@/theme';

const LoginPageSubTitle = ({ text }: { text: string }) => (
  <Typography 
    variant="body1"
    sx={{ 
      marginBottom: '40px',
      fontSize: '20px',
      textAlign: 'center',
      color: theme.palette.text.secondary
    }}
  >
    {text}
  </Typography>
);

export default LoginPageSubTitle;
