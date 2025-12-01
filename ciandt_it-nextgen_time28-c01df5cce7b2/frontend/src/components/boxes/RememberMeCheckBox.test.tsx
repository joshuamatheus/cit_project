import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import '@testing-library/jest-dom'; 
import RememberMeCheckbox from './RememberMeCheckbox';

describe('RememberMeCheckbox Component', () => {
  it('renders with correct label', () => {
    const { getAllByLabelText } = render(<RememberMeCheckbox checked={false} onChange={() => {}} />);
    const labels = getAllByLabelText('Remember me');
    expect(labels.length).toBeGreaterThan(0); 
  });

  it('renders checked when checked prop is true', () => {
    const { getAllByRole } = render(<RememberMeCheckbox checked={true} onChange={() => {}} />);
    const checkboxes = getAllByRole('checkbox');
    expect(checkboxes[0]).toBeChecked(); 
  });

  it('renders unchecked when checked prop is false', () => {
    const { getAllByRole } = render(<RememberMeCheckbox checked={false} onChange={() => {}} />);
    const checkboxes = getAllByRole('checkbox');
    expect(checkboxes[0]).not.toBeChecked(); 
  });

  it('calls onChange when changed', () => {
    const handleChange = vi.fn();
    const { getAllByRole } = render(<RememberMeCheckbox checked={false} onChange={handleChange} />);
    const checkboxes = getAllByRole('checkbox');
    fireEvent.click(checkboxes[0]); 
    expect(handleChange).toHaveBeenCalledTimes(1); 
  });

});