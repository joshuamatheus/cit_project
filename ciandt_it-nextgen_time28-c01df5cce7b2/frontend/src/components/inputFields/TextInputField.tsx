import React from 'react';
import { TextField, InputAdornment } from '@mui/material';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import theme from '@/theme';

interface TextInputFieldProps {
  icon: IconDefinition;
  placeholder: string;
  type: string;
  value: string;
  marginBottom?:string;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

const TextInputField: React.FC<TextInputFieldProps> = ({ icon, placeholder, marginBottom, type, value, onChange }) => {
  return (
    <TextField
      fullWidth
      placeholder={placeholder}
      type={type}
      value={value}
      onChange={onChange}
      variant="outlined"
      sx={{
        mb: marginBottom || '20px',
        "& .MuiOutlinedInput-root": {
          "& fieldset": {
            borderColor: theme.palette.borders.white,  // Borda do TextField
          },
          "&:hover fieldset": {
            borderColor: theme.palette.borders.white,  // Borda ao passar o mouse
          },
          "&.Mui-focused fieldset": {
            borderColor: theme.palette.borders.white,  // Borda quando focado
          },
          color: theme.palette.text.secondary,  // Cor do texto
          "& input": {
            color: theme.palette.text.secondary,  // Cor do texto dentro do TextField
          },
          "& ::placeholder": {
            color: theme.palette.text.secondary,  // Cor do placeholder
            opacity: 1,      // Opacidade do placeholder
          },
        },
      }}
      InputProps={{
        startAdornment: (
        <InputAdornment position="start">
        <FontAwesomeIcon icon={icon} className="text-white" />
        </InputAdornment>
        ),
        className: "text-white border-white",
        }}
    />
  );
};

export default TextInputField;