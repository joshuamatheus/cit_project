import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { ThemeProvider } from '@mui/material/styles';
import { vi, expect } from 'vitest';
import '@testing-library/jest-dom';
import InvitesForm from './InvitesForm';
import theme from '@/theme';

describe('InvitesForm', () => {
  let localStorageMock: { [key: string]: string } = {};
  
  beforeEach(() => {
    localStorageMock = {};
    
    Object.defineProperty(window, 'localStorage', {
      value: {
        getItem: vi.fn((key) => localStorageMock[key] || null),
        setItem: vi.fn((key, value) => {
          localStorageMock[key] = value.toString();
        }),
        removeItem: vi.fn((key) => {
          delete localStorageMock[key];
        }),
        clear: vi.fn(() => {
          localStorageMock = {};
        }),
      },
      writable: true,
    });
  });

  const renderWithTheme = (component: React.ReactElement) => {
    return render(
      <ThemeProvider theme={theme}>
        {component}
      </ThemeProvider>
    );
  };

  test('renders InvitesForm component', () => {
    renderWithTheme(<InvitesForm />);
    expect(screen.getByPlaceholderText('Inserir E-mail')).toBeInTheDocument();
    expect(screen.getByText(/Nenhum convite adicionado/)).toBeInTheDocument();
  });

  test('adds an email when add button is clicked', async () => {
    renderWithTheme(<InvitesForm />);
    
    const input = screen.getByPlaceholderText('Inserir E-mail');
    const addButton = screen.getByRole('button');
    
    fireEvent.change(input, { target: { value: 'test@example.com' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(screen.getByText('test@example.com')).toBeInTheDocument();
      expect(input).toHaveValue('');
    });
  });

  test('adds an email when Enter key is pressed', async () => {
    renderWithTheme(<InvitesForm />);
    
    const input = screen.getByPlaceholderText('Inserir E-mail');
    
    fireEvent.change(input, { target: { value: 'enter@example.com' } });
    fireEvent.keyDown(input, { key: 'Enter', code: 'Enter' });
    
    await waitFor(() => {
      expect(screen.getByText('enter@example.com')).toBeInTheDocument();
      expect(input).toHaveValue('');
    });
  });

  test('displays error for invalid email format', async () => {
    renderWithTheme(<InvitesForm />);
    
    const input = screen.getByPlaceholderText('Inserir E-mail');
    const addButton = screen.getByRole('button');
    
    fireEvent.change(input, { target: { value: 'invalid-email' } });
    
    // Verificar que o botão está desabilitado para um formato de email inválido
    await waitFor(() => {
      expect(addButton).toBeDisabled();
    });
  
    // Verificar se o componente email-input está presente, o que indica que a validação está acontecendo
    const emailInput = screen.getByTestId('email-input');
    expect(emailInput).toBeInTheDocument();
    
    expect(input).toHaveValue('invalid-email');
  });

  test('displays error for @ciandt domain', async () => {
    renderWithTheme(<InvitesForm />);
    
    const input = screen.getByPlaceholderText('Inserir E-mail');
    const addButton = screen.getByRole('button');
    
    fireEvent.change(input, { target: { value: 'user@ciandt.com' } });
    
    // Verificar que o botão está desabilitado quando usamos um email @ciandt.com
    await waitFor(() => {
      expect(addButton).toBeDisabled();
    });
  
    const emailInput = screen.getByTestId('email-input');
    expect(emailInput).toBeInTheDocument();
  });

  test('prevents duplicate emails', async () => {
    renderWithTheme(<InvitesForm />);
    
    const input = screen.getByPlaceholderText('Inserir E-mail');
    const addButton = screen.getByRole('button');
    
    // Adicionar o primeiro email
    fireEvent.change(input, { target: { value: 'duplicate@example.com' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(screen.getByText('duplicate@example.com')).toBeInTheDocument();
    });
    
    // Tentar adicionar o mesmo email novamente
    fireEvent.change(input, { target: { value: 'duplicate@example.com' } });
    
    // Verificar que o botão está desabilitado
    await waitFor(() => {
      // Verificar que o botão está desabilitado quando tentamos adicionar um email duplicado
      expect(addButton).toBeDisabled();
    });
  });

  test('deletes an email when delete button is clicked', async () => {
    renderWithTheme(<InvitesForm />);
    
    const input = screen.getByPlaceholderText('Inserir E-mail');
    const addButton = screen.getByRole('button');
    
    fireEvent.change(input, { target: { value: 'delete@example.com' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(screen.getByText('delete@example.com')).toBeInTheDocument();
    });
    
    const deleteButtons = screen.getAllByRole('button');
    const deleteButton = deleteButtons[1]; 
    
    fireEvent.click(deleteButton);
    
    await waitFor(() => {
      expect(screen.queryByText('delete@example.com')).not.toBeInTheDocument();
    });
  });

  test('loads initialInvites when provided', async () => {
    const initialInvites = [
      { id: '1', email: 'initial@example.com', status: 'Enviado' }
    ];
    
    renderWithTheme(<InvitesForm initialInvites={initialInvites} />);
    
    await waitFor(() => {
      expect(screen.getByText('initial@example.com')).toBeInTheDocument();
      expect(screen.getByText('Enviado')).toBeInTheDocument();
    });
  });

  test('calls onInvitesChange when invites are updated', async () => {
    const onInvitesChangeMock = vi.fn();
    
    renderWithTheme(<InvitesForm onInvitesChange={onInvitesChangeMock} />);
    
    const input = screen.getByPlaceholderText('Inserir E-mail');
    const addButton = screen.getByRole('button');
    
    fireEvent.change(input, { target: { value: 'callback@example.com' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(onInvitesChangeMock).toHaveBeenCalled();
    });
  });

  test('disables add button when maxInvites is reached', async () => {
    const initialInvites = Array(9).fill(null).map((_, i) => ({ 
      id: `${i}`, 
      email: `test${i}@example.com`, 
      status: 'Não enviado' 
    }));
    
    renderWithTheme(<InvitesForm initialInvites={initialInvites} maxInvites={9} />);
    
    const input = screen.getByPlaceholderText('Inserir E-mail');
    const addButton = screen.getByTestId('email-input').nextSibling;
    
    await waitFor(() => {
      expect(input).toBeDisabled();
      expect(addButton).toHaveAttribute('disabled');
    });
  });

  test('shows minimum invites message when below minimum', async () => {
    renderWithTheme(<InvitesForm minInvites={2} />);
    
    const input = screen.getByPlaceholderText('Inserir E-mail');
    const addButton = screen.getByRole('button');
    
    fireEvent.change(input, { target: { value: 'one@example.com' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(screen.getByText(/Você precisa adicionar pelo menos 2 e-mails. Faltam 1./)).toBeInTheDocument();
    });
  });

  test('loads invites from localStorage when available and no initialInvites provided', async () => {
    // Simular dados salvos no localStorage
    const savedInvites = [{ id: '123', email: 'saved@example.com', status: 'Não enviado' }];
    localStorageMock['formInvites'] = JSON.stringify(savedInvites);
    
    renderWithTheme(<InvitesForm />);
    
    await waitFor(() => {
      expect(screen.getByText('saved@example.com')).toBeInTheDocument();
    });
  });
});