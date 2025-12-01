import React from 'react';
import { ActionButton } from '../buttons/ActionButton';
import { 
  Box, 
  Typography, 
  TextField,  
  Button, 
  Divider,
  Table, 
  TableBody, 
  TableCell, 
  TableContainer, 
  TableHead, 
  TableRow,
  Paper
} from '@mui/material';
import theme from '@/theme';
import StepTitleCard from './StepTitleCard';

interface DescriptionBoxProps {
  comments: string;
}

const DescriptionBox: React.FC<DescriptionBoxProps> = ({ comments }) => {
  return (
    <Box sx={{ px: 2 }}> 
      <Typography variant="body1" color={theme.palette.texts.darkPurple} fontWeight="bold" sx={{ mb: 3, display: 'inline'}}>
        Comentários
        <Typography variant="body1" sx={{ mb: 1, opacity: 0.5, display: 'inline', ml:1}}>
           (Observações da pessoa PDM)
        </Typography>
      </Typography>

      <Typography variant="body1" sx={{ mb: 2 }}>
        Descrição
      </Typography>

      <TextField
        fullWidth
        multiline
        rows={4}
        value={comments}
        variant="outlined"
        disabled
        sx={{
          '& .MuiOutlinedInput-root': {
            '& fieldset': {
              borderColor: '#e0e0e0',
            },
            '&:hover fieldset': {
              borderColor: '#bdbdbd',
            },
            '&.Mui-focused fieldset': {
              borderColor: theme => theme.palette.primary.main,
            },
          },
        }}
      />

      <Divider sx={{ my: 3 }} /> 
    </Box>
  );
};

interface FormStatusProps {
  status: 'aprovado' | 'negado' | 'aguardando aprovação';
  onEdit: () => void;
  onFeedback: () => void;
  comments: string;
  customQuestions?: string[];
  invitedPeople?: { name: string; email: string; status: string }[];
}

const FormStatusBox: React.FC<FormStatusProps> = ({ 
  status, 
  onEdit, 
  onFeedback, 
  comments, 
  customQuestions = [], 
  invitedPeople = [] 
}) => {
  return (
    <Box
      sx={{
        backgroundColor: theme.palette.background.paper,
        display: 'flex',
        flexDirection: 'column',
        padding: { xs: '16px', sm: '24px' }, 
        width: '100%',
        borderRadius: '8px', 
        boxShadow: '0px 2px 8px rgba(0, 0, 0, 0.05)', 
      }}
    >
      <StepTitleCard stepNumber={2} stepTitle="Aprovação PDM" /> 

      <Box sx={{ px: 2, mt: 2 }}> 
        <Typography variant="body1" sx={{ mb: 2, opacity: "50%" }}>
          Status
        </Typography>
        
        <Typography variant="body1" sx={{ mb: 1, color: status === 'negado' ? 'red' : theme.palette.text.primary }}>
          {status === 'aprovado' ? 'Formulário aprovado e enviado' : 
          status === 'negado' ? 'Formulário negado' : 
          'Aprovação Pendente'}
        </Typography>

        <Divider sx={{ my: 3 }} /> 
      </Box>
        
      {status === 'aprovado' ? (
        <>
          <DescriptionBox comments={comments} />
          <Box sx={{ textAlign: 'right', mt: 2, px: 2 }}> 
            <ActionButton
              label="Acompanhar coleta de feedback"
              backgroundColor="pink" 
              onClick={onFeedback}
            />
          </Box>
        </>
      ) : status === 'negado' ? (
        <>
          <DescriptionBox comments={comments} />
          <Box sx={{ textAlign: 'right', mt: 2, px: 2 }}> 
            <ActionButton
              label="Editar formulário"
              backgroundColor="pink" 
              onClick={onEdit}
            />
          </Box>
        </>
      ) : (
        <>
          {/* Renderizar Perguntas Personalizadas */}
          <Box sx={{ mt: 4, px: 2 }}> 
            <Typography variant="body1" color={theme.palette.texts.darkPurple} fontWeight="bold" sx={{ mb: 2 }}>
              Perguntas personalizadas* (Min. 1, máx. 3)
            </Typography>
            <Typography variant="body2" sx={{ mb: 3, opacity: 0.8 }}> 
              Para além das perguntas do formulário padrão de avaliação, inclua de 1 a 3 perguntas personalizadas para atender às necessidades específicas da sua avaliação.
              (Ex.: Como você avalia minha entrega técnica do último ano?)
            </Typography>
            
            <Box
              sx={{
                border: (theme) => `1px solid ${theme.palette.borders.almost_white}`,
                borderRadius: '8px', 
                overflow: 'hidden',
                mb: 4, 
              }}
            >
              <Box sx={{ p: 4 }}> 
                {customQuestions.map((question, index) => (
                  <React.Fragment key={index}>
                    <Typography variant="body1" sx={{ py: 2 }}> 
                      {index + 1}. {question}
                    </Typography>
                    {index < customQuestions.length && ( 
                      <Divider sx={{ my: 1 }} /> 
                    )}
                  </React.Fragment>
                ))}
              </Box>
            </Box>
          </Box>
  
          {/* Renderizar Pessoas Convidadas */}
          <Box sx={{ mt: 4, px: 2 }}> 
            <Typography variant="body1" color={theme.palette.texts.darkPurple} fontWeight="bold" sx={{ mb: 2 }}>
              Pessoas convidadas* (Min. 2, máx. 9)
            </Typography>
            <Typography variant="body2" sx={{ mb: 3, opacity: 0.8 }}> 
              Convide pessoas que possam oferecer insights relevantes para o seu Journey Check-in. Alinhe com seu PDM quem são as mais pertinentes para essa etapa.
            </Typography>
            <Box
              sx={{
                border: (theme) => `1px solid ${theme.palette.borders.almost_white}`,
                borderRadius: '8px',
                overflow: 'hidden',
                mb: 3, 
              }}
            >
              <Box sx={{ p: 4 }}> {/* Aumentado padding para 32px (4*8) */}
                <TableContainer 
                  component={Paper} 
                  sx={{ 
                    boxShadow: 'none',
                    border: 'none',
                  }}
                >
                  <Table>
                    <TableHead sx={{ backgroundColor: theme.palette.borders.almost_white }}>
                      <TableRow>
                        <TableCell sx={{ borderBottom: 'none', py: 2 }}>Pessoas convidadas</TableCell> 
                        <TableCell sx={{ borderBottom: 'none', py: 2 }}>E-mail</TableCell>
                        <TableCell sx={{ borderBottom: 'none', py: 2 }}>Status</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {invitedPeople.map((person, index) => (
                        <TableRow key={index} sx={(theme) => ({ 
                          borderBottom: index < invitedPeople.length  
                            ? `1px solid ${theme.palette.borders.almost_white}` 
                            : 'none' 
                        })}>
                          <TableCell sx={{ borderBottom: 'none', py: 3 }}>{person.name}</TableCell> 
                          <TableCell sx={{ borderBottom: 'none', py: 3 }}>{person.email}</TableCell>
                          <TableCell sx={{ borderBottom: 'none', py: 3 }}>{person.status}</TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              </Box>
            </Box>
          </Box>
        </>
      )}
    </Box>
  );
};

export default FormStatusBox;