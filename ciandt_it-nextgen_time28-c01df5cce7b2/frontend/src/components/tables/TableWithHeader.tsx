'use client';

import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Box, useTheme } from '@mui/material';
import MenuBox from '../boxes/MenuBox';
import { ActionIconButton } from '../buttons/ActionButton';

export interface Column<T> {
  id: keyof T | 'actions';
  label: string;
  minWidth?: number;
  align?: 'right' | 'left' | 'center';
  format?: (value: any) => string; 
}

export interface Action<T> {
  name: string;
  icon: React.ReactNode;
  onClick: (item: T) => void;
  isVisible?: (item: T) => boolean;
}

interface TableProps<T> {
    title: string,
    description?: string,
    data: T[];
    columns: Column<T>[];
    actions?: Action<T>[];
}

function TableWithHeader<T extends Record<string, any>>({ title, description, data, columns, actions = [] }: TableProps<T>) {
  const theme = useTheme();
  
  const renderTableCell = (item: T, column: Column<T>) => {
    if (column.id === 'actions') {
      return (
        <Box sx={{ display: 'flex', justifyContent: column.align === 'center' ? 'center' : 'flex-start', gap: 1 }}>
          {actions.map((action, index) => (
            action.isVisible ? (
              action.isVisible(item) && (
                <ActionIconButton
                  key={index}
                  icon={action.icon}
                  onClick={() => action.onClick(item)}
                />
              )
            ) : (
              <ActionIconButton
                key={index}
                icon={action.icon}
                onClick={() => action.onClick(item)}
              />
            )
          ))}
        </Box>
      );
    }
    const value = item[column.id as keyof T];
    return column.format ? column.format(value) : value;
  };

  return (
    <MenuBox>
      <Box>
        <Typography variant="h5" mb={3}>
            {title}
        </Typography>
        <Typography mb={3}>
            {description}
        </Typography>
        <TableContainer sx={{ borderRadius: 2 }}>
          <Table sx={{ borderCollapse: 'separate', borderSpacing: 0 }}>
            <TableHead sx={{ backgroundColor: theme.palette.background.default }}>
              <TableRow>
                {columns.map((column, index) => (
                  <TableCell
                    key={String(column.id)}
                    align={column.align}
                    style={{ minWidth: column.minWidth }}
                    sx={{
                      ...(index === 0 && { borderTopLeftRadius: 8 }),
                      ...(index === columns.length - 1 && { borderTopRightRadius: 8 }),
                    }}
                  >
                    {column.label}
                  </TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {data.map((item, rowIndex) => (
                <TableRow 
                  key={rowIndex}
                  sx={{ '& td': { borderBottom: `1px solid ${theme.palette.divider}` } }}
                >
                  {columns.map((column) => (
                    <TableCell 
                      key={String(column.id)} 
                      align={column.align}
                    >
                      {renderTableCell(item, column)}
                    </TableCell>
                  ))}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </MenuBox>
  );
}

export default TableWithHeader;
