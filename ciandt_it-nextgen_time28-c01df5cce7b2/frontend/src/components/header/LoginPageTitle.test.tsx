import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import Title from './LoginPageTitle';

describe('Title Component', () => {
  it('renders the title text', () => {
    const titleText = 'Welcome to My App';
    
    // Renderiza o componente Title com o texto fornecido
    render(<Title text={titleText} />);
    
    // Verifica se o elemento existe e tem o texto correto
    const titleElement = screen.getByRole('heading', { name: titleText });
    expect(titleElement).toBeDefined();
    expect(titleElement.textContent).toBe(titleText);

  });
});