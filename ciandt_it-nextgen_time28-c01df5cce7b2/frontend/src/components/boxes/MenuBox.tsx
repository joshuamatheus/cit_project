import React from 'react';
import { Box } from '@mui/material';


interface MenuBoxProps {
    border?: string;
    children: React.ReactNode;
}

const MenuBox: React.FC<MenuBoxProps> = ({ border, children }) => {
    return (
        <Box  
            sx={{ 
                display: 'flex',
                flexDirection: 'column',
                gap: '32px',
                backgroundColor: 'background.paper',
                padding: '24px',
                borderRadius: '8px',
                marginBottom: '40px',
                border: border,
            }}
        >
            {children}
        </Box>
    );
};

export default MenuBox;
