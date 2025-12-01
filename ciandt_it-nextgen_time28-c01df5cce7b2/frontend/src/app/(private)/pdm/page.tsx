'use client' 
import React, { useState, useEffect } from 'react';
import { getPdmFeedbackList } from '@/service/feedback.service';
import TableWithHeader, { Column, Action } from '@/components/tables/TableWithHeader'; 
import { Container,  Snackbar, Alert} from '@mui/material'; 
import VisibilityIcon from '@mui/icons-material/Visibility';  
import { useRouter } from 'next/navigation'; 
import { ActionButton, ActionButtonTab } from '@/components/buttons/ActionButton';
import TitlePage from '@/components/header/PageTitle'; 
import { FeedbackAdapter, DisplayPdmFeedback } from "@/adapters/pdmFeedbackListAdapter";

const columns: Column<DisplayPdmFeedback>[] = [
    { id: 'login', label: 'Login', minWidth: 141 },
    { id: 'role', label: 'Role', minWidth: 200 }, 
    { id: 'positionMap', label: 'Position Map', minWidth: 158 },
    { id: 'createdAt', label: 'Data de criação', minWidth: 180 },
    { id: 'status', label: 'Status', minWidth: 223 },
    { id: 'actions', label: 'Ação', minWidth: 200 }
];

const Pdm: React.FC = () => {
    const [pendingFeedbacks, setPendingFeedbacks] = useState<DisplayPdmFeedback[]>([]);
    const [completedFeedbacks, setCompletedFeedbacks] = useState<DisplayPdmFeedback[]>([]); 
    const router = useRouter(); 
    const [snackbar, setSnackbar] = useState({ 
        open: false, 
        message: '', 
        severity: 'success' as 'success' | 'error' 
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchPdmFeedbacks = async () => {
            try {
                setLoading(true);
                // Busca os dados originais
                const feedbacksData = await getPdmFeedbackList();
                
                // Converte para o formato de exibição usando o adapter
                const displayFeedbacks = FeedbackAdapter.toDisplayModel(feedbacksData);
                
                // Filtra usando os métodos de conveniência do adapter
                const pending = FeedbackAdapter.getPendingFeedbacks(displayFeedbacks);
                const completed = FeedbackAdapter.getCompletedFeedbacks(displayFeedbacks);
                
                // Atualiza os estados
                setPendingFeedbacks(pending);
                setCompletedFeedbacks(completed);
            } catch (error) {
                console.error("Erro ao buscar feedbacks:", error);
                setSnackbar({
                    open: true,
                    message: "Erro ao carregar os feedbacks",
                    severity: "error"
                });
            } finally {
                setLoading(false);
            }
        };
        fetchPdmFeedbacks();
    }, []);

    // Função para analisar formulários pendentes
    const handleAnalyzeForm = (feedback: DisplayPdmFeedback) => {
        if (feedback && feedback.id) {//aguardando criacao end-point
            //router.push(`/feedback/analyze?id=${encodeURIComponent(feedback.id)}`);
        }
    };
    // Função para apenas visualizar formulários finalizados
    const handleViewForm = (feedback: DisplayPdmFeedback) => {
        if (feedback && feedback.id) {//aguardando criacao end-point
            //router.push(`/feedback/view?id=${encodeURIComponent(feedback.id)}`);
        }
    };
    const handleCloseSnackbar = () => {
        setSnackbar(prev => ({ ...prev, open: false }));
    };
    const pendingActions: Action<DisplayPdmFeedback>[] = [
        {
            name: 'Analisar formulário',
            icon: <ActionButton 
                    label="Analisar formulário" 
                    backgroundColor="blue" 
                    onClick={() => {}}
                  />,
            onClick: handleAnalyzeForm,
            isVisible: (feedback) => feedback.status !== 'Aguardando respostas'
        }
    ];
    const completedActions: Action<DisplayPdmFeedback>[] = [
        {
            name: 'Visualizar',
            icon: <VisibilityIcon />, 
            onClick: handleViewForm
        }
    ];

    return (
        <>
            <Container> 
                <ActionButtonTab label="Meus feedback"/> 
                <ActionButtonTab label="Meu time"/> 
                <TitlePage label="Meu time"/> 
                <TableWithHeader 
                    title="Pendentes" 
                    description="Formulários de feedback disponíveis para análise ou resposta."
                    data={pendingFeedbacks} 
                    columns={columns} 
                    actions={pendingActions} 
                />                
                <TableWithHeader 
                    title="Encerrados" 
                    description="Formulários de feedback finalizados pela pessoa avaliada."
                    data={completedFeedbacks} 
                    columns={columns} 
                    actions={completedActions}
                />
            </Container>
            <Snackbar 
                open={snackbar.open} autoHideDuration={6000} onClose={handleCloseSnackbar}>
                <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </>
    );
};
export default Pdm; 