'use client'
import { useState } from 'react';
import { ActionButtonTab } from "@/components/buttons/ActionButton";
import UserForm from "@/components/forms/UserForm";
import TitlePage from "@/components/header/PageTitle";
import { Container, Snackbar, Alert } from "@mui/material";
import { CreateUserDTO } from '@/DTOs/user/CreateUserDTO';
import { useRouter } from 'next/navigation';
import { createUser } from '@/service/user.service';
interface ValidationError {
  errors: Record<string, string>;
  message?: string;
}

export default function CreateUser() {
    const router = useRouter();
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState(false);
    const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

    const handleCreateUser = async (userData: Omit<CreateUserDTO, 'id'>) => {
        try {
            const response = await createUser(userData);
            
            // Verifica se a resposta foi sucesso (seja ID ou objeto { success: true })
            console.log('User created successfully:', response);
            setSuccess(true);
            setTimeout(() => {
                router.push('/admin');
            }, 2000);
        } catch (error) {
            if (typeof error === 'object' && error !== null && 'errors' in error) {
                const validationError = error as ValidationError;
                console.error('Validation errors:', validationError.errors);
                
                const errorMessages = Object.values(validationError.errors).join(', ');
                setError('Validation failed: ' + errorMessages);
            } else if (error instanceof Error) {
                console.error('Error creating user:', error.message);
                setError(error.message);
            } else {
                console.error('An unknown error occurred');
                setError('An unknown error occurred');
            }
        }
    };

    return (
        <Container>
            <ActionButtonTab label="Admin"/>
            <TitlePage label="Admin"/>
            <UserForm mode='create' onSubmit={handleCreateUser}/>
            <Snackbar open={!!error} autoHideDuration={6000} onClose={() => setError(null)}>
                <Alert onClose={() => setError(null)} severity="error" sx={{ width: '100%' }}>
                    {error}
                </Alert>
            </Snackbar>
            <Snackbar open={success} autoHideDuration={6000} onClose={() => setSuccess(false)}>
                <Alert onClose={() => setSuccess(false)} severity="success" sx={{ width: '100%' }}>
                    User created successfully!
                </Alert>
            </Snackbar>
        </Container>
    );
}