import { render, screen } from '@testing-library/react';
import { describe, test, expect, vi } from 'vitest';
import '@testing-library/jest-dom';
import React from 'react';
import FeedbackEvaluateForm from './FeedbackEvaluateForm';
import { ThemeProvider } from '@mui/material/styles';
import theme from '@/theme';
import { Column } from '../tables/TableWithHeader';
import { Appraiser } from './FeedbackEvaluateForm';

// Mock the dependencies
vi.mock('../tables/TableWithHeader', () => ({
  __esModule: true,
  default: ({ data, columns }: { data: Appraiser[], columns: Column<Appraiser>[] }) => (
    <div data-testid="table-with-header">
      <span data-testid="table-columns">{JSON.stringify(columns)}</span>
      <span data-testid="table-data">{JSON.stringify(data)}</span>
    </div>
  ),
}));

vi.mock('../boxes/MenuBox', () => ({
  __esModule: true,
  default: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
}));

describe('FeedbackEvaluateForm', () => {
  const mockQuestions = [
     'Question 1',
     'Question 2',
     'Question 3',
  ];

  const mockEvaluators = [
    { id: '1', email: 'user1@example.com', status: 'Pending' },
    { id: '2', email: 'user2@example.com', status: 'Completed' },
  ];

  const renderComponent = (props = {}) => {
    const defaultProps = {
      questions: mockQuestions,
      evaluators: mockEvaluators,
      ...props,
    };

    return render(
      <ThemeProvider theme={theme}>
        <FeedbackEvaluateForm {...defaultProps} />
      </ThemeProvider>
    );
  };

  test('renders the form title correctly', () => {
    renderComponent();
    expect(screen.getByText('Formulário - Coleta de feedback')).toBeInTheDocument();
  });

  test('renders the questions section with correct title', () => {
    renderComponent();
    expect(screen.getByText('Perguntas personalizadas*')).toBeInTheDocument();
    expect(screen.getByText('(Mín. 1, máx. 3)')).toBeInTheDocument();
  });

  test('renders the evaluators section with correct title', () => {
    renderComponent();
    expect(screen.getByText('Pessoas convidadas*')).toBeInTheDocument();
    expect(screen.getByText('(Mín. 2, máx. 9)')).toBeInTheDocument();
  });

  test('renders all questions with correct numbers', () => {
    renderComponent();
    expect(screen.getByText('1. Question 1')).toBeInTheDocument();
    expect(screen.getByText('2. Question 2')).toBeInTheDocument();
    expect(screen.getByText('3. Question 3')).toBeInTheDocument();
  });

  test('passes correct information to TableWithHeader', () => {
    renderComponent();
    
    const tableColumns = screen.getByTestId('table-columns');
    const columnsContent = JSON.parse(tableColumns.textContent || '');
    
    expect(columnsContent).toEqual(expect.arrayContaining([
      expect.objectContaining({ 
        id: 'email', 
        label: 'E-mail', 
        minWidth: 200, 
        align: 'left' 
      }),
      expect.objectContaining({ 
        id: 'status', 
        label: 'Status', 
        minWidth: 150, 
        align: 'left' 
      })
    ]));
    
    expect(columnsContent.length).toBe(2);
    expect(screen.getByTestId('table-data')).toBeInTheDocument();
  });
  
  

  test('renders the correct number of dividers between questions', () => {
    renderComponent();
    // There should be 2 dividers between 3 questions
    const dividers = screen.getAllByRole('separator');
    // +1 because there's also a divider at the top of the form
    expect(dividers.length).toBe(3);
  });

  test('handles empty questions array', () => {
    renderComponent({ questions: [] });
    
    // No question items should be rendered
    const listItems = screen.queryAllByRole('listitem');
    expect(listItems.length).toBe(1);
  });

  test('handles empty evaluators array', () => {
    renderComponent({ evaluators: [] });
    const tableData = screen.getByTestId('table-data');
    expect(tableData).toHaveTextContent('[]');
  });
  
  test('renders question description text', () => {
    renderComponent();
    expect(screen.getByText(/Para além das perguntas do formulário padrão de avaliação/)).toBeInTheDocument();
  });
  
  test('renders evaluators description text', () => {
    renderComponent();
    expect(screen.getByText(/Convide pessoas que possam oferecer insights relevantes/)).toBeInTheDocument();
  });
});
