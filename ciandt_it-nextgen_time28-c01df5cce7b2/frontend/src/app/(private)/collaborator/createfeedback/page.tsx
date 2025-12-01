'use client'
import { Box, Container } from '@mui/material';
import { ActionButtonTab } from '@/components/buttons/ActionButton';
import FeedbackRequestForm from '@/components/forms/FeedbackRequestForm';
import FeedbackSideMenu from '@/components/menu/FeedbackSideMenu';

const FeedbackFormScreen = () => {
  
  return (
    <Container>    
      <ActionButtonTab label="Meus Feedbacks"/>    
      <Box  
        sx={{
          display: 'flex',
          width:'100%',
          gap: '50px',
        }}
      >
        <FeedbackSideMenu/>
        <FeedbackRequestForm/>
      </Box>
    </Container>
  );
};

export default FeedbackFormScreen;
