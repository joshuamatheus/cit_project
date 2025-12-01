import React from 'react';
import { render, screen, fireEvent, within } from '@testing-library/react';
import { ThemeProvider } from '@mui/material/styles';
import FormAnalysisBox from './FormAnalysisBox';
import { vi, describe, expect } from 'vitest';
import theme from '@/theme';

const renderWithTheme = (ui: React.ReactNode) => {
  return render(
    <ThemeProvider theme={theme}>
      {ui}
    </ThemeProvider>
  );
};

describe('FormAnalysisBox', () => {
  test('renders with default values', () => {
    renderWithTheme(<FormAnalysisBox />);
    
    expect(screen.getByText('Análise do formulário')).toBeDefined();
    expect(screen.getByText('Você aprova o envio desse formulário?')).toBeDefined();
    expect(screen.getByText('Caso tenha selecionado \'Não\', complemente sua resposta:')).toBeDefined();
  });


  test('calls onDataChange when approval changes to Não', () => {
    const onDataChangeMock = vi.fn();
    renderWithTheme(<FormAnalysisBox onDataChange={onDataChangeMock} />);
    
    const selectElement = screen.getByRole('combobox');
    fireEvent.mouseDown(selectElement);
    
    const naoOption = screen.getByRole('option', { name: 'Não' });
    fireEvent.click(naoOption);
    
    expect(onDataChangeMock).toHaveBeenCalledWith('Não', '');
  });

  test('enables comment field when approval is Não', () => {
    renderWithTheme(<FormAnalysisBox />);
    
    const textField = screen.getByPlaceholderText('Deixe observações sobre o que pode ser revisado no formulário.');
    expect(textField.hasAttribute('disabled')).toBe(true);
    
    const selectElement = screen.getByRole('combobox');
    fireEvent.mouseDown(selectElement);
    
    const naoOption = screen.getByRole('option', { name: 'Não' });
    fireEvent.click(naoOption);
    
    expect(textField.hasAttribute('disabled')).toBe(false);
  });

  test('disables comment field when approval is Sim', () => {
    renderWithTheme(<FormAnalysisBox />);
    
    const selectElement = screen.getByRole('combobox');
    fireEvent.mouseDown(selectElement);
    
    const naoOption = screen.getByRole('option', { name: 'Não' });
    fireEvent.click(naoOption);
    
    const textField = screen.getByPlaceholderText('Deixe observações sobre o que pode ser revisado no formulário.');
    expect(textField.hasAttribute('disabled')).toBe(false);
    
    fireEvent.mouseDown(selectElement);
    
    const simOption = screen.getByRole('option', { name: 'Sim' });
    fireEvent.click(simOption);
    
    expect(textField.hasAttribute('disabled')).toBe(true);
  });

  test('calls onDataChange when comments change', () => {
    const onDataChangeMock = vi.fn();
    renderWithTheme(<FormAnalysisBox onDataChange={onDataChangeMock} />);
    
    const selectElement = screen.getByRole('combobox');
    fireEvent.mouseDown(selectElement);
    
    const naoOption = screen.getByRole('option', { name: 'Não' });
    fireEvent.click(naoOption);
    
    const textField = screen.getByPlaceholderText('Deixe observações sobre o que pode ser revisado no formulário.');
    fireEvent.change(textField, { target: { value: 'Test comment' } });
    
    expect(onDataChangeMock).toHaveBeenCalledWith('Não', 'Test comment');
  });

  test('clears comments when approval changes from Não to Sim', () => {
    renderWithTheme(<FormAnalysisBox />);
    
    const selectElement = screen.getByRole('combobox');
    fireEvent.mouseDown(selectElement);
    
    const naoOption = screen.getByRole('option', { name: 'Não' });
    fireEvent.click(naoOption);
    
    const textField = screen.getByPlaceholderText('Deixe observações sobre o que pode ser revisado no formulário.');
    fireEvent.change(textField, { target: { value: 'Test comment' } });
    
    fireEvent.mouseDown(selectElement);
    
    const simOption = screen.getByRole('option', { name: 'Sim' });
    fireEvent.click(simOption);
    
    const updatedTextField = screen.getByPlaceholderText('Deixe observações sobre o que pode ser revisado no formulário.') as HTMLInputElement;
    expect(updatedTextField.value).toBe('');
  });

  test('disables submit button when approval is Não and comments are empty', () => {
    renderWithTheme(<FormAnalysisBox />);
    
    const submitButton = screen.getByRole('button', { name: /Enviar Análise/i }) as HTMLButtonElement;
    expect(submitButton.disabled).toBe(false);
    
    const selectElement = screen.getByRole('combobox');
    fireEvent.mouseDown(selectElement);
    
    const naoOption = screen.getByRole('option', { name: 'Não' });
    fireEvent.click(naoOption);
    
    expect(submitButton.disabled).toBe(true);
    
    const textField = screen.getByPlaceholderText('Deixe observações sobre o que pode ser revisado no formulário.');
    fireEvent.change(textField, { target: { value: 'Test comment' } });
    
    expect(submitButton.disabled).toBe(false);
  });

  test('enables submit button when approval is Sim', () => {
    renderWithTheme(<FormAnalysisBox />);
    
    const submitButton = screen.getByRole('button', { name: /Enviar Análise/i }) as HTMLButtonElement;
    expect(submitButton.disabled).toBe(false);
  });

  test('calls onBack when back button is clicked', () => {
    const onBackMock = vi.fn();
    renderWithTheme(<FormAnalysisBox onBack={onBackMock} />);
    
    const backButton = screen.getByText('Voltar');
    fireEvent.click(backButton);
    
    expect(onBackMock).toHaveBeenCalled();
  });
});
