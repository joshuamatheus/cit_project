import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, test, expect, vi } from 'vitest';
import '@testing-library/jest-dom';
import { ThemeProvider } from '@mui/material/styles';
import QuestionsForm from './QuestionsForm';
import theme from '@/theme';

describe('QuestionsForm', () => {
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

  test('renders QuestionsForm component', () => {
    renderWithTheme(<QuestionsForm />);
    expect(screen.getByPlaceholderText('Inserir pergunta')).toBeInTheDocument();
  });

  test('adds a question when add button is clicked', async () => {
    renderWithTheme(<QuestionsForm />);
    
    const input = screen.getByPlaceholderText('Inserir pergunta');
    const addButton = screen.getByRole('button', { name: "" });
    
    fireEvent.change(input, { target: { value: 'New test question' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(screen.getByText('1. New test question')).toBeInTheDocument();
      expect(input).toHaveValue('');
    });
  });

  test('adds a question when Enter key is pressed', async () => {
    renderWithTheme(<QuestionsForm />);
    
    const input = screen.getByPlaceholderText('Inserir pergunta');
    
    fireEvent.change(input, { target: { value: 'Test question with enter' } });
    fireEvent.keyDown(input, { key: 'Enter', code: 'Enter' });
    
    await waitFor(() => {
      expect(screen.getByText('1. Test question with enter')).toBeInTheDocument();
      expect(input).toHaveValue('');
    });
  });

  test('deletes a question when delete button is clicked', async () => {
    renderWithTheme(<QuestionsForm />);
    
    const input = screen.getByPlaceholderText('Inserir pergunta');
    const addButton = screen.getByRole('button', { name: "" });
    
    fireEvent.change(input, { target: { value: 'Question to delete' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(screen.getByText('1. Question to delete')).toBeInTheDocument();
    });
    
    const deleteButtons = screen.getAllByRole('button', { name: "" });
    // The delete button is the second button (first is add button)
    const deleteButton = deleteButtons[1]; 
    
    fireEvent.click(deleteButton);
    
    await waitFor(() => {
      expect(screen.queryByText('1. Question to delete')).not.toBeInTheDocument();
    });
  });

  test('disables input and add button when max questions reached', async () => {
    renderWithTheme(<QuestionsForm />);
    
    const input = screen.getByPlaceholderText('Inserir pergunta');
    const addButton = screen.getByRole('button', { name: "" });
    
    fireEvent.change(input, { target: { value: 'Question 1' } });
    fireEvent.click(addButton);
    
    fireEvent.change(input, { target: { value: 'Question 2' } });
    fireEvent.click(addButton);
    
    fireEvent.change(input, { target: { value: 'Question 3' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(input).toBeDisabled();
      expect(addButton).toBeDisabled();
    });
  });

  test('loads saved questions from localStorage on mount', async () => {
    const savedData = {
      questions: [
        { id: 1, text: 'Saved question 1' },
        { id: 2, text: 'Saved question 2' }
      ],
      lastUpdated: new Date().toISOString()
    };
    
    localStorageMock['user_custom_questions'] = JSON.stringify(savedData);
    
    renderWithTheme(<QuestionsForm />);
    
    await waitFor(() => {
      expect(screen.getByText('1. Saved question 1')).toBeInTheDocument();
      expect(screen.getByText('2. Saved question 2')).toBeInTheDocument();
    });
  });

  test('saves questions to localStorage when questions change', async () => {
    renderWithTheme(<QuestionsForm />);
    
    const input = screen.getByPlaceholderText('Inserir pergunta');
    const addButton = screen.getByRole('button', { name: "" });
    
    fireEvent.change(input, { target: { value: 'New question to save' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(window.localStorage.setItem).toHaveBeenCalled();
    });
    
    const savedData = JSON.parse(localStorageMock['user_custom_questions']);
    expect(savedData.questions).toHaveLength(1);
    expect(savedData.questions[0].text).toBe('New question to save');
  });

  test('does not add empty questions', async () => {
    renderWithTheme(<QuestionsForm />);
    
    const input = screen.getByPlaceholderText('Inserir pergunta');
    const addButton = screen.getByRole('button', { name: "" });
    
    fireEvent.change(input, { target: { value: '   ' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(screen.queryByText(/^\d+\.\s+$/)).not.toBeInTheDocument();
    });
  });

  test('handles localStorage errors gracefully', async () => {
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});
    
    vi.spyOn(window.localStorage, 'getItem').mockImplementation(() => {
      throw new Error('localStorage error');
    });
    
    renderWithTheme(<QuestionsForm />);
    
    await waitFor(() => {
      expect(consoleSpy).toHaveBeenCalled();
      expect(screen.getByPlaceholderText('Inserir pergunta')).toBeInTheDocument();
    });
    
    consoleSpy.mockRestore();
  });

  test('initializes with provided initialQuestions props', async () => {
    const initialQuestions = [
      { id: 1, text: 'Initial question 1' },
      { id: 2, text: 'Initial question 2' }
    ];
    
    renderWithTheme(<QuestionsForm initialQuestions={initialQuestions} />);
    
    await waitFor(() => {
      expect(screen.getByText('1. Initial question 1')).toBeInTheDocument();
      expect(screen.getByText('2. Initial question 2')).toBeInTheDocument();
    });
  });
  
  test('prefers initialQuestions over localStorage data', async () => {
    // Set up localStorage with different data
    const savedData = {
      questions: [
        { id: 1, text: 'Saved question from localStorage' }
      ],
      lastUpdated: new Date().toISOString()
    };
    
    localStorageMock['user_custom_questions'] = JSON.stringify(savedData);
    
    // Provide initialQuestions prop
    const initialQuestions = [
      { id: 1, text: 'Initial provided question' }
    ];
    
    renderWithTheme(<QuestionsForm initialQuestions={initialQuestions} />);
    
    await waitFor(() => {
      // Should show initialQuestions, not localStorage data
      expect(screen.getByText('1. Initial provided question')).toBeInTheDocument();
      expect(screen.queryByText('1. Saved question from localStorage')).not.toBeInTheDocument();
    });
  });
  
  test('correctly sets idCounter based on max ID from initialQuestions', async () => {
    const initialQuestions = [
      { id: 5, text: 'Question with higher id' },
      { id: 2, text: 'Question with lower id' }
    ];
    
    renderWithTheme(<QuestionsForm initialQuestions={initialQuestions} />);
    
    const input = screen.getByPlaceholderText('Inserir pergunta');
    
    // Localizar o botão Add pelo ícone
    const addIcon = screen.getByTestId('AddIcon');
    const addButton = addIcon.closest('button');
    
    if (!addButton) {
      throw new Error('Add button not found');
    }
    
    fireEvent.change(input, { target: { value: 'New question' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      // If idCounter was set correctly, this should be id 6
      expect(screen.getByText('3. New question')).toBeInTheDocument();
    });
    
    // Verify that localStorage was updated with the correct IDs
    const savedData = JSON.parse(localStorageMock['user_custom_questions'] || '{"questions":[]}');
    expect(savedData.questions).toHaveLength(3);
    expect(savedData.questions[2].id).toBe(6);
  });
  
  test('calls onQuestionsChange when questions are modified', async () => {
    const mockOnQuestionsChange = vi.fn();
    
    renderWithTheme(
      <QuestionsForm onQuestionsChange={mockOnQuestionsChange} />
    );
    
    const input = screen.getByPlaceholderText('Inserir pergunta');
    const addButton = screen.getByRole('button', { name: "" });
    
    fireEvent.change(input, { target: { value: 'Test callback question' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(mockOnQuestionsChange).toHaveBeenCalled();
      expect(mockOnQuestionsChange).toHaveBeenCalledWith([
        expect.objectContaining({ text: 'Test callback question' })
      ]);
    });
  });
  
  test('does not save to localStorage when onQuestionsChange is provided', async () => {
    const mockOnQuestionsChange = vi.fn();
    
    renderWithTheme(
      <QuestionsForm onQuestionsChange={mockOnQuestionsChange} />
    );
    
    const input = screen.getByPlaceholderText('Inserir pergunta');
    const addButton = screen.getByRole('button', { name: "" });
    
    fireEvent.change(input, { target: { value: 'External state managed question' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(mockOnQuestionsChange).toHaveBeenCalled();
      // Verify localStorage wasn't called with this key since external state management is used
      expect(localStorageMock['user_custom_questions']).toBeUndefined();
    });
  });
  
  test('deleting a question triggers onQuestionsChange', async () => {
    const initialQuestions = [
      { id: 1, text: 'Question to be deleted' }
    ];
    
    const mockOnQuestionsChange = vi.fn();
    
    renderWithTheme(
      <QuestionsForm 
        initialQuestions={initialQuestions} 
        onQuestionsChange={mockOnQuestionsChange} 
      />
    );
    
    // Wait for initial render
    await waitFor(() => {
      expect(screen.getByText('1. Question to be deleted')).toBeInTheDocument();
    });
    
    // Find and click delete button using the DeleteIcon testId
    const deleteIcon = screen.getByTestId('DeleteIcon');
    const deleteButton = deleteIcon.closest('button');
    
    // Verificar se o botão foi encontrado
    if (!deleteButton) {
      throw new Error('Delete button not found');
    }
    
    fireEvent.click(deleteButton);
    
    await waitFor(() => {
      // Should have been called with empty array
      expect(mockOnQuestionsChange).toHaveBeenCalledWith([]);
    });
  });
  
  test('handles both initialQuestions and onQuestionsChange properly', async () => {
    const initialQuestions = [
      { id: 10, text: 'Initial question for managed component' }
    ];
    
    const mockOnQuestionsChange = vi.fn();
    
    renderWithTheme(
      <QuestionsForm 
        initialQuestions={initialQuestions} 
        onQuestionsChange={mockOnQuestionsChange} 
      />
    );
    
    // Wait for initial render with initialQuestions
    await waitFor(() => {
      expect(screen.getByText('1. Initial question for managed component')).toBeInTheDocument();
    });
    
    // Verify onQuestionsChange was called with initialQuestions
    expect(mockOnQuestionsChange).toHaveBeenCalledWith(initialQuestions);
    
    // Add another question
    const input = screen.getByPlaceholderText('Inserir pergunta');
    
    // Buscar o botão Add pelo ícone correspondente
    const addIcon = screen.getByTestId('AddIcon');
    const addButton = addIcon.closest('button');
    
    if (!addButton) {
      throw new Error('Add button not found');
    }
    
    fireEvent.change(input, { target: { value: 'Additional managed question' } });
    fireEvent.click(addButton);
    
    await waitFor(() => {
      // Verify the callback was called with both questions
      expect(mockOnQuestionsChange).toHaveBeenCalledWith([
        expect.objectContaining({ id: 10, text: 'Initial question for managed component' }),
        expect.objectContaining({ text: 'Additional managed question', id: 11 })
      ]);
    });
  });
});
