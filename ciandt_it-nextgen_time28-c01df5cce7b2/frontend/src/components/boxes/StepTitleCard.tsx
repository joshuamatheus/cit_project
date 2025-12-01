// components/CardStepTitle.tsx

import React from 'react';
import { Box, Typography } from '@mui/material';

interface StepTitlePropsCard {
  stepNumber: number;
  stepTitle: string;
}

const StepTitleCard: React.FC<StepTitlePropsCard> = ({ stepNumber, stepTitle }) => {
  return (
    <Box>
      <Typography variant="subtitle1" fontWeight="bold" sx={{ mb: 1, opacity: "70%" }} align="center">
        Etapa {stepNumber} de 4
      </Typography>
      <Typography variant="h6" fontWeight="bold" color="#020050" align="center">
        {stepTitle}
      </Typography>
    </Box>
  );
};

export default StepTitleCard;