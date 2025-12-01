// FeedbackEvaluateForm.tsx
'use client';

import React from 'react';
import { Box, Typography, List, ListItem, ListItemText, Divider } from '@mui/material';
import TableWithHeader, { Column } from '../tables/TableWithHeader';
import theme from '@/theme';
import MenuBox from '../boxes/MenuBox';

export interface Appraiser {
  id: string;
  email: string;
  status: string;
}

interface FeedbackFormProps {
  questions: string[];
  appraiserStatus?: Map<string, string> | Record<string, string>;
}

const FeedbackForm: React.FC<FeedbackFormProps> = ({ questions, appraiserStatus = {} }) => {
 
  const evaluators: Appraiser[] = appraiserStatus ? 
    Object.entries(appraiserStatus).map(([email, status], index) => ({
      id: `appraiser-${index}`,
      email: email,
      status: status
    })) : [];

  const columns: Column<Appraiser>[] = [
    { id: 'email', label: 'E-mail', minWidth: 200, align: 'left' },
    { id: 'status', label: 'Status', minWidth: 150, align: 'left' }
  ];

  return (
    <MenuBox>
      <Typography variant="h5" sx={{ color: theme.palette.texts.darkPurple, fontWeight: 'bold' }}>
        Formulário - Coleta de feedback
      </Typography>
      <Divider sx={{ marginBottom: '20px', color: theme.palette.borders.almost_white }} />
        <Box>
          <Typography 
            variant="subtitle1" 
            sx={{ 
              color: theme.palette.texts.darkPurple,
              fontWeight: 'bold',
              mb: 2,
            }}
          >
            Perguntas personalizadas* <Typography component="span" sx={{ color: theme.palette.texts.gray, fontWeight: 'normal' }}>(Mín. 1, máx. 3)</Typography>
          </Typography>
          
          <Typography variant="body2" sx={{ mb: 2 }}>
            Para além das perguntas do formulário padrão de avaliação, inclua de 1 a 3 perguntas personalizadas para atender às necessidades específicas da sua avaliação.
            (Ex.: Como você avalia minha entrega técnica do último ano/projeto?)
          </Typography>
          
          <MenuBox border='1px solid #f0f0f0'>
          <List>
            {questions && questions.length > 0 ? (
              questions.map((question, index) => (
                <React.Fragment key={index}>
                  <ListItem>
                    <ListItemText 
                      primary={`${index + 1}. ${question}`}
                    />
                  </ListItem>
                  {index < questions.length - 1 && <Divider />}
                </React.Fragment>
              ))
            ) : (
              <ListItem>
                <ListItemText primary="Nenhuma pergunta personalizada definida." />
              </ListItem>
            )}
          </List>
          </MenuBox>
        </Box>
        <Box>
          <Typography 
            variant="subtitle1" 
            sx={{ 
              color: theme.palette.texts.darkPurple,
              fontWeight: 'bold',
              mb: 2
            }}
          >
            Pessoas convidadas* <Typography component="span" sx={{ color: theme.palette.texts.gray, fontWeight: 'normal' }}>(Mín. 2, máx. 9)</Typography>
          </Typography>
          
          <Typography variant="body2" sx={{ mb: 2 }}>
            Convide pessoas que possam oferecer insights relevantes para o seu Journey Check-in. Alinhe com seu PDM quem são as mais pertinentes para essa etapa.
          </Typography>
          <MenuBox border='1px solid #f0f0f0'>
          <TableWithHeader
            title=""
            data={evaluators}
            columns={columns}
          />
          </MenuBox>
        </Box>
    </MenuBox>
  );
};

export default FeedbackForm;
