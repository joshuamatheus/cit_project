import React, { useEffect, useState } from 'react';
import { Typography, Box, Divider } from '@mui/material';
import CreateUserInputField from '@/components/inputFields/createUserInputField';
import { ActionButton, ActionButtonText } from '@/components/buttons/ActionButton';
import MenuBox from '@/components/boxes/MenuBox';
import { userTypes, roles, positionMaps, IUserForm } from './UserFormData';
import { EditUserDTO } from '@/DTOs/user/EditUserDTO';
import theme from '@/theme';


type FormErrors = {
    [K in keyof Omit<EditUserDTO, 'id'>]?: string
} & {
    general?: string
};

const UserForm: React.FC<IUserForm> = ({ mode, userData, onSubmit }) => {
    const [formData, setFormData] = useState<Omit<EditUserDTO, 'id'>>({
        name: '',
        email: '',
        type: '',
        pdmEmail: '',
        role: '',
        positionMap: '',
    });

    const [errors, setErrors] = useState<FormErrors>({});
    const [isFormIncomplete, setIsFormIncomplete] = useState<boolean>(false);
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    useEffect(() => {
        if (mode === 'edit' && userData) {
            setFormData({
                name: userData.name,
                email: userData.email,
                type: userData.type,
                pdmEmail: userData.pdmEmail,
                role: userData.role,
                positionMap: userData.positionMap,
            });
        }
    }, [mode, userData]);

    const handleChange = (field: keyof typeof formData) => (event: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [field]: event.target.value });
        if (errors[field]) {
            setErrors({ ...errors, [field]: undefined });
        }
    };

    const validateForm = () => {
        const newErrors: FormErrors = {};
        let isIncomplete = false;

        Object.entries(formData).forEach(([key, value]) => {
            console.log(key);
            if (!value && key!= 'pdmEmail') {
                newErrors[key as keyof typeof formData] = 'This field is required';
                isIncomplete = true;
            }
        });
        
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (formData.email && !emailRegex.test(formData.email)) {
            newErrors.email = 'Invalid email format';
        }

        setErrors(newErrors);
        setIsFormIncomplete(isIncomplete);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async () => {
        if (validateForm()) {
            setIsSubmitting(true);
            try {
                await onSubmit(formData);
                setErrors({});
                setIsFormIncomplete(false);
            } catch (error) {
                let newErrors: FormErrors = {};
    
                if (error && typeof error === 'object' && 'errors' in error) {
                    const validationErrors = error.errors as Record<string, string>;
                    Object.entries(validationErrors).forEach(([key, value]) => {
                        if (key in formData) {
                            newErrors[key as keyof Omit<EditUserDTO, 'id'>] = value;
                        } else if (key === 'general') {
                            newErrors.general = value;
                        }
                    });
                } else if (error instanceof Error) {
                    newErrors.general = error.message || 'An unknown error occurred';
                } else {
                    newErrors.general = 'An unknown error occurred';
                }
    
                console.error('Submission error:', newErrors);
                setErrors(newErrors);
            } finally {
                setIsSubmitting(false);
            }
        }
    };

    const onClickHelpButton = () => {
        console.log('Help');
    };

    return (
        <MenuBox>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography variant="h6" sx={{ opacity: '70%' }}>
                    {mode === 'create' ? 'Novo Cadastro' : 'Edição'}
                </Typography>
                <ActionButtonText label='Ajuda?' color='pink' onClick={onClickHelpButton} />
            </Box>

            <Typography variant="h4" sx={{ marginBottom: '20px', color: theme.palette.texts.darkPurple }}>
                {mode === 'create' ? 'Cadastro de Persona' : 'Edição de Persona'}
            </Typography>

            <Divider sx={{ marginBottom: '20px', color: theme.palette.borders.almost_white }} />

            <CreateUserInputField
                label="Name"
                placeholder="Enter your name"
                value={formData.name}
                onChange={handleChange('name')}
                error={errors.name}
            />
            <CreateUserInputField
                label="Email"
                placeholder="Enter your email"
                type="email"
                value={formData.email}
                onChange={handleChange('email')}
                error={errors.email}
            />
            <CreateUserInputField
                label="User Type"
                select
                value={formData.type}
                onChange={handleChange('type')}
                options={userTypes}
                error={errors.type}
            />
            <CreateUserInputField
                label="PDM"
                placeholder="Enter your PDM"
                value={formData.pdmEmail}
                onChange={handleChange('pdmEmail')}
                error={errors.pdmEmail}
                required={false}
            />
            <CreateUserInputField
                label="Role"
                select
                value={formData.role}
                onChange={handleChange('role')}
                options={roles}
                error={errors.role}
            />
            <CreateUserInputField
                label="Position Map"
                select
                value={formData.positionMap}
                onChange={handleChange('positionMap')}
                options={positionMaps}
                error={errors.positionMap}
            />

            {isFormIncomplete && (
                <Typography variant="body2" sx={{ color: theme.palette.error.main, marginBottom: '16px' }}>
                    Please fill all required fields
                </Typography>
            )}

            {errors.general && (
                <Typography variant="body2" sx={{ color: theme.palette.error.main, marginBottom: '16px' }}>
                    {errors.general}
                </Typography>
            )}

            <Box sx={{ display: 'flex', justifyContent: 'flex-end', marginTop: 'auto' }}>
                <ActionButton
                    label={mode === 'create' ? 'Finalizar' : 'Atualizar'}
                    backgroundColor='blue'
                    onClick={handleSubmit}
                    disabled={isSubmitting}
                />
            </Box>
        </MenuBox>
    );
};

export default UserForm;
