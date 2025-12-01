import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import TextInputField from './TextInputField';
import { faUser } from '@fortawesome/free-solid-svg-icons';

describe('TextInputField Component', () => {
  it('renders with correct props and handles changes', () => {
    const mockOnChange = vi.fn();
    const props = {
      icon: faUser,
      placeholder: 'Enter username',
      type: 'text',
      value: '',
      onChange: mockOnChange
    };

    render(<TextInputField {...props} />);

    const inputElement = screen.getByPlaceholderText('Enter username');
    expect(inputElement).toBeDefined();
    expect(inputElement.getAttribute('type')).toBe('text');

    const iconElement = screen.getByRole('img', { hidden: true });
    expect(iconElement).toBeDefined();
  });

  it('applies correct styles', () => {
    const props = {
      icon: faUser,
      placeholder: 'Enter username',
      type: 'text',
      value: '',
      onChange: vi.fn(),
      marginBottom: '30px'
    };

    const { container } = render(<TextInputField {...props} />);

    const textField = container.querySelector('.MuiTextField-root');
    expect(textField).toBeDefined();
    if (textField) {
      const computedStyle = window.getComputedStyle(textField);
      expect(computedStyle.marginBottom).toBe('30px');
    }

    const inputElement = container.querySelector('input');
    expect(inputElement).toBeDefined();
    if (inputElement) {
      const inputComputedStyle = window.getComputedStyle(inputElement);
      expect(inputComputedStyle.color).toBe('rgb(255, 255, 255)');
    }
  });
});