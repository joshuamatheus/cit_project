// LoginButton.tsx
import React from 'react';
import { Button, useTheme } from '@mui/material';

interface LoginButtonProps {
  label: string;
  onClick?: () => void;
  type?: 'button' | 'submit' | 'reset';
}

const LoginButton: React.FC<LoginButtonProps> = ({ label, onClick, type = 'button' }) => {
  const theme = useTheme();
  
  return (
    <Button
      variant="contained"
      type={type}
      fullWidth
      onClick={onClick}
      sx={{
        backgroundColor: theme.palette.error.main,
        color: theme.palette.text.secondary,
        height: '52px',
        '&:hover': {
          backgroundColor: theme.palette.error.dark,
        },
      }}
    >
      {label}
    </Button>
  );
};

export default LoginButton;
