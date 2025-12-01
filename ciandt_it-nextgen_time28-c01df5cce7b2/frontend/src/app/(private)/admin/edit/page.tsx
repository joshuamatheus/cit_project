'use client'

import { useState, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { ActionButtonTab } from "@/components/buttons/ActionButton";
import UserForm from "@/components/forms/UserForm";
import PageTitle from "@/components/header/PageTitle";
import { EditUserDTO } from "@/DTOs/user/EditUserDTO";
import { Container, Snackbar, Alert, CircularProgress } from "@mui/material";
import { fetchWrapper } from '@/utils/fetchApi';
import { updateUser } from '@/service/user.service';

export default function UserEdit() {
    const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });
    const [userData, setUserData] = useState<EditUserDTO | null>(null);
    const [loading, setLoading] = useState(true);
    const router = useRouter();
    const searchParams = useSearchParams();

    useEffect(() => {
        const fetchUserData = async () => {
            const id = searchParams.get('id');
            console.log(id);
            if (id) {
                try {
                    const user = await fetchWrapper<EditUserDTO>(`/users/${encodeURIComponent(id)}`, {
                        method: 'GET',
                    });
                    setUserData(user);
                } catch (error) {
                    console.error('Failed to fetch user data:', error);
                    setSnackbar({ open: true, message: 'Failed to fetch user data', severity: 'error' });
                } finally {
                    setLoading(false);
                }
            } else {
                setLoading(false);
                setSnackbar({ open: true, message: 'No email provided', severity: 'error' });
            }
        };

        fetchUserData();
    }, [searchParams]);

    const handleUpdateUser = async (updatedUserData: Omit<EditUserDTO, 'id'>) => {
        if (!userData) return;

        try {
            const requestBody = {
                name: updatedUserData.name,
                email: updatedUserData.email,
                type: updatedUserData.type,
                positionMap: updatedUserData.positionMap.toUpperCase(),
                role: updatedUserData.role,
                pdmEmail: updatedUserData.pdmEmail,
            };
    
            const updatedUser = await fetchWrapper<EditUserDTO>(`/users/${userData.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBody),
            });
            setSnackbar({ open: true, message: 'User updated successfully', severity: 'success' });
            router.push('/admin');
        } catch (error) {
            if (error instanceof Error) {
                try {
                    const errorData = JSON.parse(error.message);
                    setSnackbar({ open: true, message: errorData.message, severity: 'error' });
                } catch {
                    setSnackbar({ open: true, message: error.message, severity: 'error' });
                }
            } else {
                setSnackbar({ open: true, message: 'An unknown error occurred', severity: 'error' });
            }
        }
    };

    const handleCloseSnackbar = (event?: React.SyntheticEvent | Event, reason?: string) => {
        if (reason === 'clickaway') {
            return;
        }
        setSnackbar({ ...snackbar, open: false });
    };

    if (loading) {
        return <CircularProgress />;
    }

    return (
        <div>
            <Container>
                <ActionButtonTab label="Admin"/>
                <PageTitle label="Admin"/>
                {userData ? (
                    <UserForm mode='edit' userData={userData} onSubmit={handleUpdateUser}/>
                ) : (
                    <p>User not found</p>
                )}
            </Container>
            <Snackbar open={snackbar.open} autoHideDuration={6000} onClose={handleCloseSnackbar}>
                <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </div>
    );
}
