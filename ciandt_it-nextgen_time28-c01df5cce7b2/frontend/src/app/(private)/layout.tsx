import Header from '@/components/header/header';
import { Box } from '@mui/material';
export default function PrivateLayout({ children }: { children: React.ReactNode }) {
    return (
        <Box>
            <Header/>
            <Box>{children}</Box>
        </Box>
    );
}
