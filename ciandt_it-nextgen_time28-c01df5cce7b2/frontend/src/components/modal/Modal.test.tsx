import React, { ReactElement } from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import Modal from './Modal';
import { beforeEach, describe, expect, test, vi } from 'vitest';
import '@testing-library/jest-dom';
import { ThemeProvider } from '@mui/material/styles';
import theme from '@/theme';

describe('Modal Component', () => {
    const mockOnClose = vi.fn();
    const mockOnConfirm = vi.fn();

    beforeEach(() => {
        vi.clearAllMocks();
    });

    const renderWithTheme = (component: ReactElement) => {
        return render(
            <ThemeProvider theme={theme}>
                {component}
            </ThemeProvider>
        );
    };

    test('renders Modal with correct title and message', () => {
        renderWithTheme(
            <Modal
                open={true}
                onClose={mockOnClose}
                onConfirm={mockOnConfirm}
                title="Test Title"
                message="This is a test message."
                button1="Cancel"
                button2="Confirm"
            />
        );

        expect(screen.getByText('Test Title')).toBeInTheDocument();
        expect(screen.getByText('This is a test message.')).toBeInTheDocument();
    });

    test('renders buttons with correct labels', () => {
        renderWithTheme(
            <Modal
                open={true}
                onClose={mockOnClose}
                onConfirm={mockOnConfirm}
                title="Test Title"
                message="This is a test message."
                button1="Cancel"
                button2="Confirm"
            />
        );

        expect(screen.getByText('Cancel')).toBeInTheDocument();
        expect(screen.getByText('Confirm')).toBeInTheDocument();
    });

    test('calls onClose when Cancel button is clicked', () => {
        renderWithTheme(
            <Modal
                open={true}
                onClose={mockOnClose}
                onConfirm={mockOnConfirm}
                title="Test Title"
                message="This is a test message."
                button1="Cancel"
                button2="Confirm"
            />
        );

        fireEvent.click(screen.getByText('Cancel'));
        expect(mockOnClose).toHaveBeenCalledTimes(1);
    });

    test('calls onConfirm when Confirm button is clicked', () => {
        renderWithTheme(
            <Modal
                open={true}
                onClose={mockOnClose}
                onConfirm={mockOnConfirm}
                title="Test Title"
                message="This is a test message."
                button1="Cancel"
                button2="Confirm"
            />
        );

        fireEvent.click(screen.getByText('Confirm'));
        expect(mockOnConfirm).toHaveBeenCalledTimes(1);
    });

    test('does not render Modal when open is false', () => {
        renderWithTheme(
            <Modal
                open={false}
                onClose={mockOnClose}
                onConfirm={mockOnConfirm}
                title="Test Title"
                message="This is a test message."
                button1="Cancel"
                button2="Confirm"
            />
        );

        expect(screen.queryByText('Test Title')).not.toBeInTheDocument();
        expect(screen.queryByText('This is a test message.')).not.toBeInTheDocument();
    });
});
