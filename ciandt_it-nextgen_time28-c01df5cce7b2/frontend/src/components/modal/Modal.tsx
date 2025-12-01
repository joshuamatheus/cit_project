import React from 'react';
import { Dialog, DialogActions, DialogContent, DialogTitle, Box } from '@mui/material';
import { ActionButton, ActionButtonText } from '@/components/buttons/ActionButton';

interface PopupProps {
    open: boolean;
    onClose: () => void;
    onConfirm: () => void;
    title: string;
    message: string;
    button1: string;
    button2?: string;
}

const Modal: React.FC<PopupProps> = ({ open, onClose, onConfirm, title, message, button1, button2 }) => {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>{title}</DialogTitle>
            <DialogContent>
                <div>{message}</div>
            </DialogContent>
            <Box  sx={{padding: '10px'}}>
            <DialogActions>
                <ActionButtonText label={button1} color='black' onClick={onClose}/>
                <ActionButton label={button2} backgroundColor='pink' onClick={onConfirm}/>
            </DialogActions>
            </Box>
        </Dialog>
    );
};

export default Modal;