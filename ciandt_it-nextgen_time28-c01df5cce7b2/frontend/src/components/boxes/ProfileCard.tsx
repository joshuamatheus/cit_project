import React from 'react';
import { Card, CardContent, Typography, Avatar, Box } from '@mui/material';
import { UserProfileDTO } from '@/DTOs/user/UserProfileDTO';


const ProfileCard: React.FC<{ user: UserProfileDTO }> = ({ user }) => {
  if (!user) {
    return null; 
  }

  return (
    <Card variant="outlined" sx={{ width:'100%', margin: 'auto'}}>
      <CardContent sx={{ display: 'flex', alignItems: 'center' }}> 
        <Avatar sx={{ bgcolor: 'primary.main', width: 40, height: 40, marginRight: 2 }}>
          {user.name.charAt(0)} 
        </Avatar>
        <Box>
          <Typography variant="h6" component="div" sx={{fontWeight: 'bold', fontSize: '18px'}}>
            {user.name}
          </Typography>
          <Typography variant="body2" color="texts.gray" sx={{fontSize: '18px'}}>
            @{user.email.split('@')[0]} 
          </Typography>
        </Box>
      </CardContent>
      <CardContent sx={{marginTop: '8px', paddingTop: 0, paddingBottom: 0}}> 
        <Typography variant="body1" component="div" sx={{mb: 1, fontWeight: 'bold', fontSize: '16px'}}> 
          {user.role} 
        </Typography>
        <Typography variant="body2" color="texts.gray" sx={{fontSize: '16px'}}>
          {user.positionMap} 
        </Typography>
      </CardContent>
    </Card>
  );
};

export default ProfileCard;