import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import TableC, { Action, Column } from './TableWithHeader';
import { beforeEach, describe, expect, test, vi,it } from 'vitest';
import '@testing-library/jest-dom';

describe('TableC Component', () => {
  const columns: Column<{ id: number; name: string }>[] = [
    { id: 'id', label: 'ID' },
    { id: 'name', label: 'Name' },
  ];

  const data = [
    { id: 1, name: 'Item 1' },
    { id: 2, name: 'Item 2' },
  ];

  it('renders the table title and one row of data', () => {
    render(<TableC title="Test Table" data={data} columns={columns} />);

    // Verifica se o título está presente
    expect(screen.getByText('Test Table')).toBeInTheDocument();

    // Verifica se a linha de dados está presente
    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('Item 1')).toBeInTheDocument();
  });

  it('renders the table title, description and multiple rows of data', () => {
    render(
      <TableC 
        title="Test Table" 
        description="This is a test description." 
        data={data} 
        columns={columns} 
      />
    );

    // Verifica se o título está presente
    expect(screen.getByText('Test Table')).toBeInTheDocument();

    // Verifica se a descrição está presente
    expect(screen.getByText('This is a test description.')).toBeInTheDocument();

    // Verifica se as linhas de dados estão presentes
    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('Item 1')).toBeInTheDocument();
    expect(screen.getByText('2')).toBeInTheDocument();
    expect(screen.getByText('Item 2')).toBeInTheDocument();
});

it('renders empty table when no data is provided', () => {
    render(<TableC title="Test Table" data={[]} columns={columns} />);

    // Verifica se o título está presente
    expect(screen.getByText('Test Table')).toBeInTheDocument();

    // Verifica se não há linhas de dados
    expect(screen.queryByText('1')).not.toBeInTheDocument();
    expect(screen.queryByText('Item 1')).not.toBeInTheDocument();
  });


});
