'use client'
import { ActionButtonTab } from "@/components/buttons/ActionButton"
import FeedbackRequestHeader from "@/components/header/FeedbackRequestHeader"
import { Box, Container, Typography, Snackbar, Alert, AlertColor } from "@mui/material"
import FeedbackForm from "@/components/forms/FeedbackEvaluateForm"
import FormAnalysisBox from "@/components/boxes/FormAnalysisBox"
import { getFeedbackRequestByID } from "@/service/feedback.service"
import { useEffect, useState } from "react"
import { EditUserDTO } from "@/DTOs/user/EditUserDTO"
import { getUserById } from "@/service/user.service"
import { formatDate } from "@/utils/formatDate"
import { useRouter } from "next/navigation"

const defaultAppraisersStatus = {
  "fulano@empresa.com": "Não enviado",
  "siclano@empresa.com": "Não enviado"
};

const PDMEvaluate: React.FC = () => {
    const [feedbackData, setFeedbackData] = useState<FeedbackRequestDTO | null>(null);
    const [userData, setUserData] = useState<EditUserDTO | null>(null);
    const [snackbarOpen, setSnackbarOpen] = useState<boolean>(false);
    const [snackbarMessage, setSnackbarMessage] = useState<string>('');
    const [snackbarSeverity, setSnackbarSeverity] = useState<AlertColor>('success');
    const router = useRouter();

    function onBack(): void {
        router?.push(`/pdm`)
    }
    
    function handleSuccess(): void {
        showSnackbar('Análise enviada com sucesso!', 'success');
        setTimeout(() => {
            router?.push(`/pdm`);
        }, 2000); // Redirecionar após 2 segundos para o usuário ver a mensagem
    }
    
    const showSnackbar = (message: string, severity: AlertColor = 'success') => {
        setSnackbarMessage(message);
        setSnackbarSeverity(severity);
        setSnackbarOpen(true);
    };

    const handleSnackbarClose = () => {
        setSnackbarOpen(false);
    };

    const handleError = (errorMessage: string) => {
        showSnackbar(errorMessage, 'error');
    };

    useEffect(() => {
        const getFeedbackData = async () => {
            try {
                const feedback = await getFeedbackRequestByID('0d52b879-a389-4ab8-9df9-eae09625e85f');
                if (feedback.createdAt) {
                    feedback.createdAt = formatDate(feedback.createdAt);
                }
                setFeedbackData(feedback);
                const user = await getUserById(feedback.requesterId);
                setUserData(user);
                console.log('Feedback data:', feedback);
                console.log('User:', user);
            } catch(error) {
                console.error('Error fetching feedback data:', error);
                showSnackbar('Erro ao carregar os dados do feedback.', 'error');
                
                setFeedbackData({
                    id: '0',
                    questions: ["Como você avalia meu desempenho?"],
                    appraisers: ["fulano@empresa.com", "siclano@empresa.com"],
                    appraiserStatus: defaultAppraisersStatus,
                    requesterId: '0',
                    createdAt: '01/01/2001',
                });
                setUserData({
                    id: 0,
                    name: 'string',
                    email: 'string',
                    type: 'string',
                    pdmEmail: 'string',
                    role: 'string',
                    positionMap: 'string',
                });
            }
        }
        getFeedbackData();
    }, []);

    return (
        <Container>
            <ActionButtonTab label='Meus Feedbacks'/>
            <ActionButtonTab label='Meu Time'/>
            <Box sx={{ marginBottom: 4, padding: 3, background:'#FFF7E7'}}>
                <Typography variant="body1" sx={{fontWeight: 800}}>
                    Análise de formulário pendente
                </Typography>
            </Box>
            <FeedbackRequestHeader
                person={userData?.name || ''}
                position={userData?.positionMap || ''}
                limitDate={feedbackData?.createdAt || ''}
                status="in_progress"
            />
            <FeedbackForm 
                questions={feedbackData?.questions || []}
                appraiserStatus={feedbackData?.appraiserStatus || defaultAppraisersStatus}
            />
            <FormAnalysisBox 
                onBack={onBack} 
                feedbackRequestId={feedbackData?.id || ''} 
                onSuccess={handleSuccess}
                onError={handleError}
            />
            
            <Snackbar 
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={handleSnackbarClose}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert 
                    onClose={handleSnackbarClose} 
                    severity={snackbarSeverity} 
                    sx={{ width: '100%' }}
                >
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </Container>
    )
}

export default PDMEvaluate;
