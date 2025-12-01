import React from 'react';
import { Box, TextField, Typography, MenuItem } from '@mui/material';
import theme from '@/theme';


interface InputBoxProps {
    label: string;
    placeholder?: string;
    type?: string;
    value: string;
    onChange?: (event: React.ChangeEvent<any>) => void;
    options?: { id: string; name: string }[];
    select?: boolean;
    error?: string;
    required?: boolean;
}

const CreateUserInputField: React.FC<InputBoxProps> = ({ label, placeholder, type = 'text', options, select = false, value, onChange, error, required = true }) => {
    return (
        <Box sx={{ marginBottom: '8px' }}>
             <Typography variant="body1" sx={{ fontWeight: 'bold', color: theme.palette.texts.darkPurple, marginBottom: '8px' }}>
                {label}
            </Typography>
            <Box
                sx={{
                    backgroundColor: theme.palette.background.paper,
                    border: '1px solid #f0f0f0',
                    borderRadius: '8px',
                    padding: '24px',
                }}
            >
                {select ? (
                    <TextField
                        aria-label={label}
                        fullWidth
                        select
                        variant="outlined"
                        size="small"
                        value={value}
                        onChange={onChange}
                        error={!!error}
                        helperText={error}
                        required={required}
                    >
                        {options?.map((option) => (
                            <MenuItem key={option.id} value={option.id}>
                                {option.name}
                            </MenuItem>
                        ))}
                    </TextField>
                ) : (
                    <TextField
                        aria-label={label}
                        fullWidth
                        variant="outlined"
                        size="small"
                        placeholder={placeholder}
                        type={type}
                        value={value}
                        onChange={onChange}
                        error={!!error}
                        helperText={error}
                        required={required}
                    />
                )}
            </Box>
        </Box>
    );
};

export default CreateUserInputField;
