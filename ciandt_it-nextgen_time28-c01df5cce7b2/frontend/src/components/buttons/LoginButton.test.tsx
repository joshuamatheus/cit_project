import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import LoginButton from './LoginButton';
import '@testing-library/jest-dom'; 
import { vi } from 'vitest';
import { ThemeProvider, createTheme } from '@mui/material/styles';

const theme = createTheme();

describe('LoginButton', () => {
  test('renders login button with correct text', () => {
    render(
      <ThemeProvider theme={theme}>
        <LoginButton label='Login'/>
      </ThemeProvider>
    );
    
    const buttonElement = screen.getByText('Login');
    expect(buttonElement).toBeInTheDocument();
  });

  test('calls onClick prop when clicked', () => {
    const mockOnClick = vi.fn();
    render(
      <ThemeProvider theme={theme}>
        <LoginButton label='Login' onClick={mockOnClick}/>
      </ThemeProvider>
    );
    
    const buttonElement = screen.getByText('Login');
    fireEvent.click(buttonElement);
    
    expect(mockOnClick).toHaveBeenCalledTimes(1);
  });

  test('has correct type when specified', () => {
    render(
      <ThemeProvider theme={theme}>
        <LoginButton label ='Submit' type="submit"/>
      </ThemeProvider>
    );
    
    const buttonElement = screen.getByText('Submit');
    expect(buttonElement).toHaveAttribute('type', 'submit');
  });
});