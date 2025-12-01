import { CreateUserDTO } from '@/DTOs/user/CreateUserDTO';
import { ListUserDTO } from '@/DTOs/user/ListUserDTO';
import { EditUserDTO } from '@/DTOs/user/EditUserDTO';
import { UserHasPasswordDTO } from '@/DTOs/user/UserHasPasswordDTO';
import { UserPasswordRegistrationDTO } from '@/DTOs/user/UserPasswordRegistrationDTO';
import { UserTypeResponseDTO } from '@/DTOs/user/UserTypeResponseDTO';
import { fetchWrapper } from '@/utils/fetchApi';
import Cookies from 'js-cookie';
import { TOKEN_COOKIE } from './auth.service';

// Obter todos os usuários
export const getAllUsers = async (): Promise<ListUserDTO[]> => {
  try {
    return await fetchWrapper<ListUserDTO[]>('/users/admin'); // Usando fetchWrapper
  } catch (error) {
    console.error('Error fetching users:', error);
    throw error;
  }
};

//User Deactivate
export const deleteUser = async (id: ListUserDTO['id']): Promise<void> => {
  try {
    await fetchWrapper<void>(`/users/deactivate/${id}`, {
      method: 'DELETE',
    });
  } catch (error) {
    console.error('Error deleting user:', error);
    throw error;
  }
};

export const checkUserByEmail = async (email: string): Promise<UserHasPasswordDTO> => {
  try {
    return await fetchWrapper<UserHasPasswordDTO>(`/users/by-email?email=${encodeURIComponent(email)}`, {
      method: 'GET',
      credentials: 'include',
    });
  } catch (error) {
    console.error('Error checking user by email:', error);
    throw error;
  }
};

// Registrar senha
export const registerPassword = async (
  email: string, 
  password: string, 
  confirmPassword: string
): Promise<void> => {
  try {
    const userData: UserPasswordRegistrationDTO = {
      email,
      password,
      confirmPassword
    };
    await fetchWrapper<void>('/users/register-password', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
      credentials: 'include',
    }); // Usando fetchWrapper
  } catch (error) {
    console.error('Error registering password:', error);
    throw error;
  }
};
//cadastrar/criar usuario
export const createUser = async (userData: Omit<CreateUserDTO, 'id'>): Promise<{ success: boolean } | number> => {
  try {
    const result = await fetchWrapper<{ success: boolean } | number>('/users/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
      credentials: 'include',
    });
    return result; // Pode ser { success: true } ou um número (ID)
  } catch (error) {
    console.error('Error creating user:', error);
    throw error;
  }
};
// Verificar o tipo do usuário atual
export const getCurrentUserType = async (token : string): Promise<UserTypeResponseDTO> => {
  try {
    if (!token) {
      throw new Error('Authentication token not found');
    }
    
    const response = await fetchWrapper<UserTypeResponseDTO>('/users/type', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      credentials: 'include', // Mantém para enviar cookies
    });

     // Garanta que a resposta tem a propriedade "type"
     if (!response || !response.type) {
      console.error('Invalid user type response:', response);
      throw new Error('Invalid user type response from API');
    }

    return response;

  } catch (error) {
    console.error('Error getting current user type:', error);
    throw error;
  }
};
//Editar usuario/ (Obter usuário por ID + Atualizar usuário existente)
export const getUserById = async (id: string): Promise<EditUserDTO> => {
  const token = Cookies.get(TOKEN_COOKIE);;
  try {
    return await fetchWrapper<EditUserDTO>(`/users/${encodeURIComponent(id)}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
  } catch (error) {
    console.error('Error fetching user by ID:', error);
    throw error;
  }
};
export const updateUser = async (id: number, userData: Omit<EditUserDTO, 'id'>): Promise<{ success: boolean }> => {
  try {
    return await fetchWrapper<{ success: boolean }>(`/users/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
      credentials: 'include',
    });
  } catch (error) {
    console.error('Error updating user:', error);
    throw error;
  }
};
