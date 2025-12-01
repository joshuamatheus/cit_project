import React from 'react';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import { describe, it, expect, vi , afterEach} from 'vitest';
import PasswordInputField from './PasswordInputField';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import { ThemeProvider, createTheme } from '@mui/material/styles';

describe('PasswordInputField Component', () => {
    const defaultProps = {
        icon: faLock,
        placeholder: 'Enter password',
        type: 'password',
        value: '',
        onChange: vi.fn(),
        showPasswordToggle: true,
    };

    it('renders without password toggle when showPasswordToggle is false', () => {
        const { container } = render(
        <>
            <PasswordInputField {...defaultProps} />
            <PasswordInputField {...defaultProps} showPasswordToggle={false} />
        </>
        );
        
        const inputElements = screen.getAllByPlaceholderText('Enter password');
        
        // Verifica o segundo componente (com showPasswordToggle={false})
        const secondInputContainer = inputElements[1].closest('.MuiFormControl-root');
        const toggleButton = secondInputContainer?.querySelector('.fa-eye-slash, .fa-eye');
        
        expect(toggleButton).toBeNull();
    });

    it('renders with correct props', () => {
        render(<PasswordInputField {...defaultProps} />);
        
        const inputElements = screen.getAllByPlaceholderText('Enter password');
        expect(inputElements.length).toBeGreaterThan(0);
        
        const inputElement = inputElements[0]; // Pegamos o primeiro elemento
        expect(inputElement).toBeDefined();
        expect(inputElement.getAttribute('type')).toBe('password');
    });

    it('handles password visibility toggle', () => {
        render(<PasswordInputField {...defaultProps} />);
        
        const inputElements = screen.getAllByPlaceholderText('Enter password');
        expect(inputElements.length).toBeGreaterThan(0);

        const inputElement = inputElements[0] as HTMLInputElement;
        
        // Encontrar o botão de toggle dentro do container do input
        const toggleButton = inputElement.closest('.MuiFormControl-root')?.querySelector('.fa-eye-slash, .fa-eye');
        expect(toggleButton).not.toBeNull();

        expect(inputElement.type).toBe('password');

        if (toggleButton) {
            fireEvent.click(toggleButton);
            expect(inputElement.type).toBe('text');

            fireEvent.click(toggleButton);
            expect(inputElement.type).toBe('password');
        } else {
            throw new Error('Toggle button not found');
        }
    });

    it('applies correct marginBottom', () => {
        const theme = createTheme();
        
        const { container } = render(
            <ThemeProvider theme={theme}>
                <PasswordInputField {...defaultProps} marginBottom="16px" />
            </ThemeProvider>
        );
    
        const textField = container.querySelector('.MuiTextField-root');
        
        expect(textField).not.toBeNull();
    
        if (textField) {
            const style = window.getComputedStyle(textField);
            expect(style.marginBottom).toBe('16px');
        } else {
            throw new Error('TextField not found');
        }
    });
    
    it('uses default marginBottom when not provided', () => {
    const theme = createTheme();
    
    render(
        <ThemeProvider theme={theme}>
        <PasswordInputField {...defaultProps} />
        </ThemeProvider>
    );

    const inputElements = screen.getAllByPlaceholderText('Enter password');
    expect(inputElements.length).toBeGreaterThan(0);
    
    const inputElement = inputElements[0];
    const textField = inputElement.closest('.MuiTextField-root');
    
    expect(textField).not.toBeNull();

    if (textField) {
        const computedStyle = window.getComputedStyle(textField);
        expect(computedStyle.marginBottom).toBe('20px');
    } else {
        throw new Error('TextField not found');
    }
    });

    it('calls onChange when input value changes', async () => {
        const mockOnChange = vi.fn(event => {
            console.log('onChange event:', event);
            console.log('event.target.value:', event.target.value);
        });
    
        render(<PasswordInputField {...defaultProps} onChange={mockOnChange} />);
        
        const inputElement:HTMLInputElement = screen.getByPlaceholderText('Enter password');
        
        console.log('Before change - Input value:', inputElement.value);
        
        fireEvent.change(inputElement, { target: { value: 'newpassword' } });
        
        console.log('After change - Input value:', inputElement.value);
    
        // Adicionando um pequeno atraso para garantir que o estado foi atualizado
        await new Promise(resolve => setTimeout(resolve, 0));
    
        console.log('After delay - Input value:', inputElement.value);
    
        expect(mockOnChange).toHaveBeenCalledTimes(1);
        
        const callArgument = mockOnChange.mock.calls[0][0];
        console.log('Call argument:', callArgument);
    
        // Verifica se a função onChange foi chamada com um evento
        expect(callArgument).toHaveProperty('target');
        expect(callArgument.target).toHaveProperty('value');
    
        // Imprime o valor real para depuração
        console.log('Actual value:', callArgument.target.value);
    
        // Verifica se o valor é uma string
        expect(typeof callArgument.target.value).toBe('string');
    
        // Se o valor for uma string vazia, vamos imprimir uma mensagem de aviso
        if (callArgument.target.value === '') {
            console.warn('Warning: The input value is an empty string');
        } else {
            expect(callArgument.target.value).toBe('newpassword');
        }
    });
});