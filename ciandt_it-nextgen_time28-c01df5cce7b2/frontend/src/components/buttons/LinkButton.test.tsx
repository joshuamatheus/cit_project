import React from 'react';
import { render } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import LinkButton from './LinkButton';
import '@testing-library/jest-dom'; 

describe('ForgotPasswordLink Component', () => {
  it('renders correctly', () => {
    const { getByText } = render(<LinkButton label='Forgot password' />);
    const linkElement = getByText('Forgot password');
    
    expect(linkElement).toBeInTheDocument(); 
    expect(linkElement).toHaveStyle('color:rgb(255, 255, 255)'); 
  });
});