import React from 'react';
import { Link } from '@mui/material';
import theme from '@/theme';

interface ILinkButton {
    label: string,
    link?: string,
}

const LinkButton: React.FC<ILinkButton> = ({label, link}) => {
  return (
    <Link href={link} 
    sx={{
      color: theme.palette.text.secondary
    }}>
      {label}
    </Link>
  );
};

export default LinkButton;