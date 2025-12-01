import React from 'react';
import { Typography, Paper } from '@mui/material';
import { styled } from '@mui/material/styles';
import { StepStatus } from '@/../../.next/types/stepStatus';

interface StepCardProps {
  currentStep: number;
  totalSteps: number;
  stepName: string;
  status: StepStatus;
}

const StyledCard = styled(Paper, {
  shouldForwardProp: (prop) => prop !== 'status',
})<{ status: StepStatus }>(({ theme, status }) => ({
  padding: theme.spacing(2),
  margin: theme.spacing(1, 0),
  borderRadius: 0,
  backgroundColor: 
    status === 'inProgress' 
      ? theme.palette.primary.main
      : theme.palette.background.paper,
  opacity: status === 'toDo' ? 0.5 : 1,
}));

const StepCard: React.FC<StepCardProps> = ({ currentStep, totalSteps, stepName, status }) => {
  return (
    <StyledCard status={status} elevation={0}>
      <Typography 
        variant="subtitle1" 
        sx={{
          color: status === 'inProgress' 
            ? theme => theme.palette.primary.contrastText
            : theme => theme.palette.primary.main,
        }}
      >
        Etapa {currentStep} de {totalSteps}
      </Typography>
      <Typography 
        variant="body1"
        sx={{
          color: status === 'inProgress' 
            ? theme => theme.palette.primary.contrastText 
            : theme => theme.palette.text.primary,
          mt: 0.5,
        }}
      >
        {stepName}
      </Typography>
    </StyledCard>
  );
};

export default StepCard;