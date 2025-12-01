'use client';

import React, { useState, useEffect } from 'react';
import { Box, TextField, Typography } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import { v4 as uuidv4 } from 'uuid';
import TableWithHeader, { Column, Action } from '../tables/TableWithHeader';
import { ActionButton } from '../buttons/ActionButton';
import MenuBox from '../boxes/MenuBox';
import theme from '@/theme';

interface Invite {
  id: string;
  email: string;
  status: string;
}

interface InvitesFormProps {
  maxInvites?: number;
  minInvites?: number;
  onInvitesChange?: (invites: Invite[]) => void;
  initialInvites?: Invite[]; // New prop for initial data
}

const InvitesForm: React.FC<InvitesFormProps> = ({ 
  maxInvites = 9,
  minInvites = 2,
  onInvitesChange,
  initialInvites = [] // Default to empty array
}) => {
  const [invites, setInvites] = useState<Invite[]>(initialInvites);
  const [email, setEmail] = useState('');
  const [emailError, setEmailError] = useState('');
  const [initialDataLoaded, setInitialDataLoaded] = useState(false);

  // Load saved invites from localStorage or use initialInvites
  useEffect(() => {
    // Only try to load from localStorage if initialInvites is empty
    // and we haven't loaded data yet
    if (initialInvites.length === 0 && !initialDataLoaded) {
      const savedInvites = localStorage.getItem('formInvites');
      if (savedInvites) {
        try {
          setInvites(JSON.parse(savedInvites));
        } catch (e) {
          console.error('Error while loading saved invites:', e);
        }
      }
    } else if (initialInvites.length > 0 && !initialDataLoaded) {
      // If initialInvites were provided and we haven't loaded them yet
      setInvites(initialInvites);
      // Update localStorage with the initial data
      localStorage.setItem('formInvites', JSON.stringify(initialInvites));
    }
    
    setInitialDataLoaded(true);
  }, [initialInvites, initialDataLoaded]);

  // Save invites to localStorage when they change
  useEffect(() => {
    if (initialDataLoaded) { // Only save after initial data is processed
      localStorage.setItem('formInvites', JSON.stringify(invites));
      if (onInvitesChange) {
        onInvitesChange(invites);
      }
    }
  }, [invites, onInvitesChange, initialDataLoaded]);

  const validateEmail = (email: string): string => {
    if (!email.trim()) {
      return 'O e-mail é obrigatório';
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      return 'Formato de e-mail inválido';
    }

    if (email.toLowerCase().includes('@ciandt')) {
      return 'Não é permitido convidar e-mails @ciandt';
    }

    if (invites.some(invite => invite.email.toLowerCase() === email.toLowerCase())) {
      return 'Este e-mail já foi convidado';
    }

    if (invites.length >= maxInvites) {
      return `Limite máximo de ${maxInvites} convites atingido`;
    }

    return '';
  };

  const addInvite = () => {
    const error = validateEmail(email);
    if (error) {
      setEmailError(error);
      return;
    }

    const newInvite: Invite = {
      id: uuidv4(),
      email: email.trim(),
      status: 'Não enviado'
    };

    setInvites([...invites, newInvite]);
    setEmail('');
    setEmailError('');
  };

  const removeInvite = (inviteToRemove: Invite) => {
    setInvites(invites.filter(invite => invite.id !== inviteToRemove.id));
  };

  const columns: Column<Invite>[] = [
    { id: 'email', label: 'E-mail', minWidth: 250 },
    { id: 'status', label: 'Status', minWidth: 100 },
    { id: 'actions', label: '', minWidth: 50, align: 'right' }
  ];

  const actions: Action<Invite>[] = [
    {
      name: 'delete',
      icon: <DeleteIcon sx={{ color: theme.palette.buttons.red }} fontSize="small" />,
      onClick: removeInvite
    }
  ];

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      addInvite();
    }
  };

  const hasMinimumInvites = invites.length >= minInvites;
  
  const getStatusMessage = () => {
    if (invites.length === 0) {
      return `Nenhum convite adicionado. Adicione pelo menos ${minInvites} e-mails.`;
    } else if (!hasMinimumInvites) {
      return `Você precisa adicionar pelo menos ${minInvites} e-mails. Faltam ${minInvites - invites.length}.`;
    }
    return null;
  };

  const isMaxInvitesReached = invites.length >= maxInvites; 
  
  return (
    <MenuBox border={`1px solid ${theme.palette.divider}`}>
      <Box sx={{
        display: 'flex', 
        width: '100%',
        mb: 2
      }}>
        <TextField
          fullWidth
          variant="outlined"
          size="small"
          placeholder="Inserir E-mail"
          value={email}
          onChange={(e) => {
            setEmail(e.target.value);
            if (emailError) setEmailError('');
          }}
          onKeyDown={handleKeyPress}
          error={!!emailError}
          helperText={emailError}
          sx={{ 
            mr: 1,
            '& .MuiOutlinedInput-root': {
              borderRadius: '4px',
              backgroundColor: theme.palette.background.paper,
            }
          }}
          data-testid="email-input"
          disabled={isMaxInvitesReached}
        />
        <ActionButton 
          icon={<AddIcon />} 
          backgroundColor='blue' 
          onClick={addInvite}
          disabled={!email.trim() || isMaxInvitesReached || validateEmail(email) != ''}
        />
      </Box>

      {invites.length > 0 ? (
        <Box>
          <TableWithHeader
            title=""
            data={invites}
            columns={columns}
            actions={actions}
          />
          {!hasMinimumInvites && (
            <Typography variant="body2" color="error" sx={{ mt: 1 }}>
              {getStatusMessage()}
            </Typography>
          )}
        </Box>
      ) : (
        <Typography variant="body2" color="text.secondary" sx={{ mt: 2 }}>
          {getStatusMessage()}
        </Typography>
      )}
    </MenuBox>
  );
};

export default InvitesForm;