import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { ActionButton, ActionButtonText, ActionButtonTab, ActionIconButton } from './ActionButton';
import { describe, test, expect, vi } from 'vitest'; 
import '@testing-library/jest-dom';


describe('ActionButton Component', () => {
    const label = 'Click Me';
    const mockOnClick = vi.fn();


    //**** Render Tests ****//

    //Regular Button
    test('renders ActionButton', () => {
        render(<ActionButton label={label} onClick={mockOnClick} />);
        expect(screen.getByText(label)).toBeInTheDocument();
        fireEvent.click(screen.getByRole('button'));
        expect(mockOnClick).toHaveBeenCalled();
    });

    //Text Button
    test('renders ActionButtonText', () => {
        render(<ActionButtonText label={label} onClick={mockOnClick} />);
        expect(screen.getByText(label)).toBeInTheDocument();
        fireEvent.click(screen.getByRole('button'));
        expect(mockOnClick).toHaveBeenCalled();
    });

    //Icon Button
    test('renders Icon Button and triggers onClick', () => {
        render(<ActionIconButton onClick={mockOnClick} />);
        expect(screen.getByRole('button')).toBeInTheDocument();
        fireEvent.click(screen.getByRole('button'));
        expect(mockOnClick).toHaveBeenCalled();
    });



    //**** Disable Tests****//


    //Regular Button
    test('disables ActionButton', () => {
        render(<ActionButton label={label} onClick={mockOnClick} disabled />);
        expect(screen.getByRole('button')).toBeDisabled();
    });

    
});
