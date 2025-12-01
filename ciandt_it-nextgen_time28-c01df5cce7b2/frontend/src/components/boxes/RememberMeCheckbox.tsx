import React from 'react';
import { FormControlLabel, Checkbox, useTheme } from '@mui/material';

interface RememberMeCheckboxProps {
  checked: boolean;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

const RememberMeCheckbox: React.FC<RememberMeCheckboxProps> = ({ checked, onChange }) => {
  const theme = useTheme();
  
  return (
    <FormControlLabel
      control={
        <Checkbox 
          checked={checked} 
          onChange={onChange} 
          sx={{
            color: theme.palette.text.secondary,
            '&.Mui-checked': {
              color: theme.palette.text.secondary, 
            },
            '&.Mui-checked:hover': {
              backgroundColor: 'transparent',
            },
          }}
        />
      }
      label="Remember me"
      sx={{
        '& .MuiFormControlLabel-label': {
          color: theme.palette.text.secondary,
          fontSize: '0.875rem',
        },
      }}
    />
  );
};

export default RememberMeCheckbox;
