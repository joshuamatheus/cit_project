import React, { useState } from 'react';
import { TextField, InputAdornment } from '@mui/material';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import theme from '@/theme';

interface PasswordInputFieldProps {
  icon: any;
  placeholder: string;
  type: string;
  value: string;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  showPasswordToggle?: boolean;
  marginBottom?: string;
}

const PasswordInputField: React.FC<PasswordInputFieldProps> = ({
  icon,
  placeholder,
  type,
  value,
  onChange,
  showPasswordToggle = false,
  marginBottom

}) => {
  const [showPassword, setShowPassword] = useState(true);

  const handleTogglePasswordVisibility = () => {
    setShowPassword((prev) => !prev);
  };

  return (
    <TextField
      fullWidth
      placeholder={placeholder}
      type={showPassword && showPasswordToggle ? type : 'text'}
      value={value}
      onChange={onChange}
      variant="outlined"
      sx={{
        mb: marginBottom || '20px',
        "& .MuiOutlinedInput-root": {
          "& fieldset": {
            borderColor: theme.palette.borders.white,
          },
          "&:hover fieldset": {
            borderColor: theme.palette.borders.white,
          },
          "&.Mui-focused fieldset": {
            borderColor: theme.palette.borders.white,
          },
          color: theme.palette.background.paper,
          "& input": {
            color: theme.palette.background.paper,
          },
          "& ::placeholder": {
            color: theme.palette.background.paper,
            opacity: 1,
          },
        },
      }}
      slotProps={{
        input: {
          startAdornment: (
            <InputAdornment position="start">
              <FontAwesomeIcon icon={icon} className="text-white" />
            </InputAdornment>
          ),
          endAdornment: showPasswordToggle && (
            <InputAdornment position="end">
              <FontAwesomeIcon 
                icon={showPassword ? faEyeSlash : faEye} 
                className="text-white cursor-pointer" 
                onClick={handleTogglePasswordVisibility} 
              />
            </InputAdornment>
          ),
        }
      }}
    />
  );
};

export default PasswordInputField;