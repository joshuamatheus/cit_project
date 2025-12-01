// ActionButton.tsx
import React from 'react';
import { Button, IconButton, useTheme } from '@mui/material';
import { IActionButton } from './ButtonInterface';

//Regular Button
export const ActionButton: React.FC<IActionButton> = ({ icon, label, backgroundColor, onClick, disabled = false }) => {
    const theme = useTheme();
    const bgColor = backgroundColor === 'blue' ? theme.palette.buttons.blue : 
                    backgroundColor === 'pink' ? theme.palette.buttons.pink : 
                    backgroundColor;
    
    return (
        <Button 
            variant="contained" 
            sx={{ 
                backgroundColor: bgColor, 
                paddingX: '8px', 
                paddingY: '6px', 
                color: theme.palette.text.secondary,  
                textTransform: 'none' 
            }}
            onClick={onClick}
            disabled={disabled}
        >
            {icon}
            {label}
        </Button>
    );
};

//Text Button
export const ActionButtonText: React.FC<IActionButton> = ({ icon, label, color, onClick }) => {
    const theme = useTheme();
    const textcolor = color === 'black' ? theme.palette.text.primary : 
                     color === 'pink' ? theme.palette.primary.main : 
                     color;
    return (
        <Button 
            variant="text" 
            sx={{ 
                color: textcolor,
                textTransform: 'none' 
            }}
            onClick={onClick}
        >
            {icon}
            {label}
        </Button>
    );
};

//Tab Button
export const ActionButtonTab: React.FC<IActionButton> = ({label, onClick, disabled = false}) => {
    const theme = useTheme();
    
    return (
        <Button 
            variant="text"
            sx={{ 
                boxShadow:'none', 
                backgroundColor: theme.palette.buttons.lightBlue,
                color: theme.palette.texts.darkPurple, 
                marginTop: '50px',
                marginBottom: '40px',
                textTransform: 'none',
            }}
            onClick={onClick}
            disabled={disabled}
        >
            {label}
        </Button>
    );
};

export const ActionIconButton: React.FC<IActionButton> = ({ icon, onClick }) => {
    // Caso especial: se o ícone for um componente que não deve ser aninhado dentro de um botão
    if (React.isValidElement(icon)) {
        const isButton = 
            icon.type === Button || 
            icon.type === IconButton || 
            icon.type === ActionButton || 
            icon.type === ActionButtonText || 
            icon.type === ActionButtonTab ||
            (typeof icon.type === 'string' && icon.type.toLowerCase() === 'button');
        
        if (isButton) {
            type IconProps = {
                onClick?: (event: React.MouseEvent) => void;
                [key: string]: any;
            };
            const iconProps = icon.props as IconProps;
            const handleClick = (e: React.MouseEvent) => {
                e.stopPropagation();
                if (iconProps.onClick) {
                    iconProps.onClick(e);
                }
                if (onClick) onClick();
            };
            return React.cloneElement(icon, {
                onClick: handleClick
            } as Partial<IconProps>);
        }
    }
    // Caso padrão: o ícone não é um botão, então apenas renderiza normalmente
    return (
        <IconButton 
            onClick={onClick} 
            size="small"
        >
            {icon}
        </IconButton>
    );
};