'use client'
import React from 'react';
import { Typography, Box, styled, useTheme } from '@mui/material';
import MenuBox from '../boxes/MenuBox';

interface FeedbackRequestHeaderProps {
  person: string;
  position: string;
  startDate?: string;
  endDate?: string;
  limitDate?: string;
  status: 'in_progress' | 'approved';
  border?: string;
}

const InfoSection = styled(Box)({
    marginRight: 50,
});

const FeedbackRequestHeader: React.FC<FeedbackRequestHeaderProps> = ({ 
  person, 
  position, 
  startDate, 
  endDate,
  limitDate,
  status,
  border = '1px solid #f0f0f0'
}) => {
  const theme = useTheme();
  
  return (
    <MenuBox border={border}>
      <Box sx={{ 
        display: 'flex',
        marginBottom: 2,
      }}>
        <InfoSection>
          <Typography 
            sx={{ 
              color: theme.palette.texts.gray,
              fontSize: '0.9rem',
              mb: 2,
            }}
          >
            Pessoa avaliada
          </Typography>
          <Typography>
            {person}
          </Typography>
        </InfoSection>
        
        <InfoSection>
          <Typography 
            sx={{ 
              color: theme.palette.texts.gray,
              fontSize: '0.9rem',
              mb: 2,
            }}
          >
            Position/Map
          </Typography>
          <Typography>
            {position}
          </Typography>
        </InfoSection>
        
        {status === 'approved' ? (
          // Display start date and end date for approved requests
          <>
            <InfoSection>
              <Typography 
                sx={{ 
                  color: theme.palette.texts.gray,
                  fontSize: '0.9rem',
                  mb: 2,
                }}
              >
                Data de criação
              </Typography>
              <Typography>
                {startDate}
              </Typography>
            </InfoSection>
            
            <InfoSection>
              <Typography 
                sx={{ 
                  color: theme.palette.texts.gray,
                  fontSize: '0.9rem',
                  mb: 2,
                }}
              >
                Data de conclusão
              </Typography>
              <Typography>
                {endDate}
              </Typography>
            </InfoSection>
          </>
        ) : (
          // Display limit date for in_progress requests
          <InfoSection>
            <Typography 
              sx={{ 
                color: theme.palette.texts.gray,
                fontSize: '0.9rem',
                mb: 2,
              }}
            >
              Data limite
            </Typography>
            <Typography>
              {limitDate}
            </Typography>
          </InfoSection>
        )}
      </Box>
    </MenuBox>
  );
};

export default FeedbackRequestHeader;
