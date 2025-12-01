import React from 'react';
import { render, fireEvent, screen, RenderOptions } from '@testing-library/react';
import { ThemeProvider } from '@mui/material/styles';
import UserForm from './UserForm';
import { describe, test, expect, vi } from 'vitest';
import '@testing-library/jest-dom';
import theme from '@/theme';


interface CustomRenderOptions extends Omit<RenderOptions, 'wrapper'> {}

const customRender = (
  ui: React.ReactElement,
  options?: CustomRenderOptions
) => {
  return render(ui, {
    wrapper: ({ children }) => (
      <ThemeProvider theme={theme}>{children}</ThemeProvider>
    ),
    ...options
  });
};

describe('UserForm', () => {
    const mockOnSubmit = vi.fn();

    test('renders in create mode', () => {
        customRender(<UserForm mode="create" onSubmit={mockOnSubmit} userData={undefined} />);
        expect(screen.getByText('Novo Cadastro')).toBeInTheDocument();
        expect(screen.getByText('Cadastro de Persona')).toBeInTheDocument();
    });

    test('renders in edit mode with user data', () => {
        const userData = { 
            id: 1, 
            name: 'John Doe', 
            email: 'john@example.com', 
            type: 'COLLABORATOR', 
            pdmEmail: '', 
            role: 'PRODUCT_DESIGNER', 
            positionMap: '' 
        };
        customRender(<UserForm mode="edit" onSubmit={mockOnSubmit} userData={userData} />);
        expect(screen.getByDisplayValue('John Doe')).toBeInTheDocument();
        expect(screen.getByDisplayValue('john@example.com')).toBeInTheDocument();
    });

    test('validates required fields', () => {
        customRender(<UserForm mode="create" onSubmit={mockOnSubmit} userData={undefined} />);
        fireEvent.click(screen.getByText('Finalizar'));
        expect(screen.getByText('Please fill all required fields')).toBeInTheDocument();
    });

    test('validates email format', () => {
        customRender(<UserForm mode="create" onSubmit={mockOnSubmit} userData={undefined} />);
        fireEvent.change(screen.getByPlaceholderText('Enter your email'), { target: { value: 'invalid-email' } });
        fireEvent.click(screen.getByText('Finalizar'));
        expect(screen.getByText('Invalid email format')).toBeInTheDocument();
    });
});