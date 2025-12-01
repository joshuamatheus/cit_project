import React, { useState, ChangeEvent } from 'react';
import { Box, Typography, TextField, MenuItem, Select, SelectChangeEvent, AlertColor } from '@mui/material';
import MenuBox from './MenuBox';
import theme from '@/theme';
import { ActionButton, ActionButtonText } from '../buttons/ActionButton';
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { reviewFeedbackRequest } from '@/service/feedback.service';

interface FormAnalysisProps {
  feedbackRequestId: string;
  onDataChange?: (approved: string, comments: string) => void;
  onBack?: () => void;
  onSuccess?: () => void;
  onError?: (errorMessage: string) => void;
}

const FormAnalysisBox: React.FC<FormAnalysisProps> = ({ 
  feedbackRequestId, 
  onDataChange, 
  onBack,
  onSuccess,
  onError
}) => {
  const [approved, setApproved] = useState<string>('Sim');
  const [comments, setComments] = useState<string>('');
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const handleApprovalChange = (event: SelectChangeEvent<string>) => {
    const newApproved = event.target.value;
    setApproved(newApproved);
    
    if (newApproved === 'Sim') {
      setComments('');
      if (onDataChange) onDataChange(newApproved, '');
    } else if (onDataChange) {
      onDataChange(newApproved, comments);
    }
  };

  const handleCommentsChange = (event: ChangeEvent<HTMLInputElement>) => {
    const newComments = event.target.value;
    setComments(newComments);
    if (onDataChange) onDataChange(approved, newComments);
  };

  const handleSubmitAnalisys = async () => {
    try {
      setIsSubmitting(true);
      setError(null);
      
      const isApproved = approved === 'Sim';
      
      await reviewFeedbackRequest(
        feedbackRequestId,
        isApproved,
        !isApproved ? comments : undefined
      );
      
      // Se houver função de callback para sucesso, chame-a
      if (onSuccess) {
        onSuccess();
      }
    } catch (err) {
      const errorMessage = 'Ocorreu um erro ao enviar a análise. Por favor, tente novamente.';
      setError(errorMessage);
      console.error('Error submitting analysis:', err);
      
      // Call onError with the error message if available
      if (onError) {
        onError(errorMessage);
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  const isCommentEnabled = approved === 'Não';

  const isButtonDisabled = approved === 'Não' && comments.trim() === '';

  return (
    <>
      <MenuBox border={`1px solid ${theme.palette.divider}`}>
          <Typography variant="h6" fontWeight="bold" sx={{ mb: 3}}>
          Análise do formulário
          </Typography>
          {/* Seção de aprovação */}
          <Box sx={{ mb: 3 }}>
            <Typography variant="body1" fontWeight="bold" sx={{ mb: 3, color: theme.palette.texts.darkPurple}}>
              Você aprova o envio desse formulário?
            </Typography>
            
            <Select
              fullWidth
              value={approved}
              onChange={handleApprovalChange}
              displayEmpty
              size="small"
              sx={{ 
                borderColor: theme => theme.palette.primary.main,
                '.MuiOutlinedInput-notchedOutline': {
                  borderColor: '#e0e0e0',
                  borderWidth: '1px',
                },
                '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                  borderColor: theme => theme.palette.primary.main,
                },
                '&:hover .MuiOutlinedInput-notchedOutline': {
                  borderColor: '#bdbdbd',
                },
                borderRadius: 1
              }}
            >
              <MenuItem value="Sim">Sim</MenuItem>
              <MenuItem value="Não">Não</MenuItem>
            </Select>
          </Box>

          {/* Seção de comentários */}
          <Box>
            <Typography variant="body1" fontWeight="bold" sx={{ mb: 3, color: theme.palette.texts.darkPurple }}>
              Caso tenha selecionado 'Não', complemente sua resposta:
            </Typography>
            
            <TextField
              fullWidth
              multiline
              rows={4}
              size="small"
              placeholder="Deixe observações sobre o que pode ser revisado no formulário."
              value={comments}
              onChange={handleCommentsChange}
              variant="outlined"
              required={isCommentEnabled}
              disabled={!isCommentEnabled}
              sx={{
                '& .MuiInputBase-input.Mui-disabled': {
                  WebkitTextFillColor: '#9e9e9e',
                },
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
          </Box>
      </MenuBox>
        {error && (
          <Typography color="error" sx={{ mt: 2, textAlign: 'center' }}>
            {error}
          </Typography>
        )}
        
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: '15px' }}>
          <ActionButtonText 
            icon={<ArrowBackIcon/>} 
            label='Voltar' 
            color='black' 
            onClick={onBack}
            disabled={isSubmitting}
          />
          <ActionButton 
            label={isSubmitting ? 'Enviando...' : 'Enviar Análise'} 
            backgroundColor="pink" 
            disabled={isButtonDisabled || isSubmitting} 
            onClick={handleSubmitAnalisys} 
          />
        </Box>
    </>
  );
};

export default FormAnalysisBox;