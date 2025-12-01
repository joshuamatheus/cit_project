import React from 'react';
import { Typography } from '@mui/material';
import theme from '@/theme';


interface IPageTitle{
    label:string
}

const PageTitle: React.FC <IPageTitle> = ({label}) => {
    return (
        <div>
                <Typography variant="h4" component="h1" sx={{fontWeight: '700', color: theme.palette.text.primary, marginLeft:'30px', marginBottom: '40px' }}>
                    {label}
                </Typography>  
        </div>
    );
};

export default PageTitle; 
