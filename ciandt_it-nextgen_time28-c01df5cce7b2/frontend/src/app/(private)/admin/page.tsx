'use client'

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ActionButtonTab } from '@/components/buttons/ActionButton';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { Alert, Container, Snackbar } from '@mui/material';
import TitlePage from '@/components/header/PageTitle';
import { CreateBox } from '@/components/boxes/CreateBox';
import TableWithHeader, { Column, Action } from '@/components/tables/TableWithHeader';
import Modal from '@/components/modal/Modal';
import { deleteUser, getAllUsers } from '@/service/user.service';
import { ListUserDTO } from '@/DTOs/user/ListUserDTO'; 
import { mapUserType} from '@/utils/enumMappings';

const columns: Column<ListUserDTO>[] = [
  { id: 'name', label: 'Nome', minWidth: 100 },
  { id: 'email', label: 'Email', minWidth: 150 },
  { 
    id: 'userType', 
    label: 'Tipo', 
    minWidth: 100,
    format: (value: string) => mapUserType(value)
  },
  { id: 'actions', label: 'Ações', minWidth: 100, align: 'center' }
];

const UserList: React.FC = () => {
  const [users, setUsers] = useState<ListUserDTO[]>([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [userToDelete, setUserToDelete] = useState<ListUserDTO | null>(null); 
  const router = typeof window !== 'undefined' ? useRouter() : null;

  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const data = await getAllUsers();
      setUsers(data);
    } catch (error) {
      console.error('Failed to fetch users:', error);
    }
  };

  const handleDeleteClick = (user: ListUserDTO) => {
    setUserToDelete(user);
    setModalOpen(true); 
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setUserToDelete(null); 
  };

  const handleConfirmDelete = async () => {
    if (userToDelete) {
      try {
        await deleteUser(userToDelete.id);
        fetchUsers();
        setSnackbar({ open: true, message: 'Usuário desativado com sucesso!', severity: 'success' });
      } catch (error) {
        setSnackbar({ open: true, message: 'Failed to deactivate user.', severity: 'error' });
      }
    }
    handleCloseModal();
  };

  const handleEditClick = (user: ListUserDTO) => {
    if (user && user.id) {
      router?.push(`/admin/edit?id=${encodeURIComponent(user.id)}`);
    } else {
      console.error('User or user id is undefined');
    }
  };

  const actions: Action<ListUserDTO>[] = [
    {
      name: 'Editar',
      icon: <EditIcon />,
      onClick: handleEditClick
    },
    {
      name: 'Deletar',
      icon: <DeleteIcon sx={{ color: '#EF4444' }} />,
      onClick: handleDeleteClick
    }
  ];

  function handleCreateUser(): void {
    console.log("Criar usuario");
    router?.push(`/admin/create`)
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
        <ActionButtonTab label="Admin"/>
        <TitlePage label="Admin"/>
        <CreateBox line1='Novo Cadastro' line2='Descrição' onCreateUser={handleCreateUser}/>
        <TableWithHeader title='Usuários Cadastrados' data={users} columns={columns} actions={actions} />

        <Modal 
          open={modalOpen} 
          onClose={handleCloseModal} 
          onConfirm={handleConfirmDelete} 
          title="Confirmar Exclusão" 
          message={`Você tem certeza que deseja excluir o usuário?`} 
          button1="Voltar" 
          button2="Excluir" 
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

export default UserList;
