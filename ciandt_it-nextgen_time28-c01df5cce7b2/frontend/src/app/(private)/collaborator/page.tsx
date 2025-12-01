"use client";

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import {  ActionButtonTab } from '@/components/buttons/ActionButton';
import { Alert, Container, Snackbar } from '@mui/material';
import TitlePage from '@/components/header/PageTitle';
import { CreateBox } from '@/components/boxes/CreateBox';
import TableWithHeader, { Column, Action } from '@/components/tables/TableWithHeader';
import Modal from '@/components/modal/Modal';
import { FeedbackListDTO } from '@/DTOs/feedbackRequest/FeedbackListDTO';
import { deleteFeedback, getAllFeedbacks } from '@/service/feedback.service';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import VisibilityIcon from '@mui/icons-material/Visibility';

const columns: Column<FeedbackListDTO>[] = [
  { id: 'createdAt', label: 'Data de criação', minWidth: 100 },
  { id: 'status', label: 'Status', minWidth: 150 },
  { id: 'actions', label: 'Ação', minWidth: 100, align: 'center' }
];

const FeedbackList: React.FC = () => {
  const [feedbacks, setFeedbacks] = useState<FeedbackListDTO[]>([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [feedbackToDelete, setFeedbackToDelete] = useState<FeedbackListDTO | null>(null); 
  const router = typeof window !== 'undefined' ? useRouter() : null;
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });
  
  useEffect(() => {
    fetchFeedbacks();
  }, []);

  const fetchFeedbacks = async () => {
    try {
      const data = await getAllFeedbacks();
      setFeedbacks(data);
    } catch (error) {
      console.error('Failed to fetch feedbacks:', error);
    }
  };

  const handleDeleteClick = (feedback: FeedbackListDTO) => {
    setFeedbackToDelete(feedback);
    setModalOpen(true); 
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setFeedbackToDelete(null); 
  };

  const handleConfirmDelete = async () => {
    if (feedbackToDelete) {
      try {
        await deleteFeedback(feedbackToDelete.id);
        fetchFeedbacks();
        setSnackbar({ open: true, message: 'Feedback excluído com sucesso!', severity: 'success' });
      } catch (error) {
        setSnackbar({ open: true, message: 'Falha ao excluir feedback.', severity: 'error' });
      }
    }
    handleCloseModal();
  };

  const handleEditClick = (feedback: FeedbackListDTO) => {
    if (feedback && feedback.id) {
      router?.push(`/feedback/edit?id=${encodeURIComponent(feedback.id)}`);
    } else {
      console.error('Feedback or feedback id is undefined');
    }
  };

  const handleViewClick = (feedback: FeedbackListDTO) => {
    if (feedback && feedback.id) {
      router?.push(`/feedback/view?id=${encodeURIComponent(feedback.id)}`);
    } else {
      console.error('Feedback or feedback id is undefined');
    }
  };

  const actions: Action<FeedbackListDTO>[] = [
    {
      name: 'Editar',
      icon: <EditIcon/>,
      onClick: handleEditClick,
      isVisible: (feedback) => ['Em criação', 'Em aprovação', 'Rejeitado'].includes(feedback.status)
    },
    {
      name: 'Visualizar',
      icon: <VisibilityIcon/>,
      onClick: handleViewClick,
      isVisible: (feedback) => ['Finalizado', 'Aguardando respostas', 'Expirado'].includes(feedback.status)
    },
    {
      name: 'Deletar',
      icon: <DeleteIcon sx={{ color: '#EF4444' }}/>,
      onClick: handleDeleteClick,
      isVisible: (feedback) => ['Rejeitado', 'Em aprovação', 'Aguardando respostas', 'Em criação'].includes(feedback.status)
    }
  ];

  function handleCreateFeedback(): void {
    router?.push(`/collaborator/createfeedback`)
  }

  const handleCloseSnackbar = (event?: React.SyntheticEvent | Event, reason?: string) => {
    if (reason === 'clickaway') {
        return;
    }
    setSnackbar({ ...snackbar, open: false });
  };

  return (
    <>
      <Container>
        <ActionButtonTab label="Feedback"/>
        <TitlePage label="Meus feedbacks"/>
        <CreateBox line1='Nova coleta de feedback' line2='Crie um formulário com perguntas personalizadas' onCreateUser={handleCreateFeedback}/>
        <TableWithHeader title='Coletas de feedback' description='Histórico dos formulários criados' data={feedbacks} columns={columns} actions={actions} />
        <Modal 
          open={modalOpen} 
          onClose={handleCloseModal} 
          onConfirm={handleConfirmDelete} 
          title="Excluir coleta" 
          message={`Ao clicar em "Excluir coleta" você não terá mais acesso ao histórico e análise de resultado dessa coleta.`} 
          button1="Excluir coleta" 
          button2="Cancelar" 
        />
      </Container>
      <Snackbar open={snackbar.open} autoHideDuration={6000} onClose={handleCloseSnackbar}>
        <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
            {snackbar.message}
        </Alert>
      </Snackbar>
    </>
  );
};
export default FeedbackList;