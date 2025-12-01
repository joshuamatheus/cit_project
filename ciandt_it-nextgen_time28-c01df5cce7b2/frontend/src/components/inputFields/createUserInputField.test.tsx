import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import InputBox from './createUserInputField';
import { describe, test, expect, vi } from 'vitest'; 
import '@testing-library/jest-dom';


describe('InputBox Component', () => {
    const mockOnChange = vi.fn();
    const label = 'Test Label';

    test('renders with label and input', () => {
        render(<InputBox label={label} />);
        expect(screen.getByText(label)).toBeInTheDocument();
        expect(screen.getByRole('textbox')).toBeInTheDocument();
    });

    test('renders with placeholder', () => {
        const placeholder = 'Enter text';
        render(<InputBox label={label} placeholder={placeholder} />);
        expect(screen.getByPlaceholderText(placeholder)).toBeInTheDocument();
    });

    test('calls onChange when typing in input', () => {
        render(<InputBox label={label} onChange={mockOnChange} />);
        fireEvent.change(screen.getByRole('textbox'), { target: { value: 'Test' } });
        expect(mockOnChange).toHaveBeenCalled();
    });
});
