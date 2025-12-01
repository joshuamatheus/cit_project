import { fetchWrapper } from '@/utils/fetchApi';
import { LoginRequestDTO } from '@/DTOs/user/LoginRequestDTO';
import { TokenResponseDTO } from '@/DTOs/user/TokenResponseDTO';
import Cookies from 'js-cookie';

export const TOKEN_COOKIE = 'token';
const TOKEN_TYPE_COOKIE = 'token_type';
const TOKEN_EXPIRATION_COOKIE = 'token_expiration';
const REMEMBER_ME_COOKIE = 'remember_me';
const SAVED_EMAIL_COOKIE = 'saved_email';

const STANDARD_EXPIRATION_DAYS = 0.16;
const REMEMBER_ME_EXPIRATION_DAYS = 30;

export const authService = {
  login: async (email: string, password: string, rememberMe: boolean = false): Promise<boolean> => {
    try {
      console.log('Making login request with email:', email, 'and rememberMe:', rememberMe);
      
      const data = await fetchWrapper<TokenResponseDTO>('/users/login', {
        method: 'POST',
        body: JSON.stringify({ email, password } as LoginRequestDTO),
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include'
      });
      
      if (!data || !data.token || !data.expiresIn || !data.tokenType) {
        console.error('Login response missing token information:', data);
        return false;
      }

      const expirationDays = rememberMe ? REMEMBER_ME_EXPIRATION_DAYS : STANDARD_EXPIRATION_DAYS;
      
      const cookieOptions = {
        expires: expirationDays,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'strict' as const
      };

      Cookies.set(REMEMBER_ME_COOKIE, rememberMe ? 'true' : 'false', cookieOptions);
      
      if (rememberMe) {
        Cookies.set(SAVED_EMAIL_COOKIE, email, cookieOptions);
      } else {
        Cookies.remove(SAVED_EMAIL_COOKIE);
      }
      
      Cookies.set(TOKEN_COOKIE, data.token, cookieOptions);
      Cookies.set(TOKEN_TYPE_COOKIE, data.tokenType, cookieOptions);
      
      const expirationTime = new Date().getTime() + (data.expiresIn * 1000); // Assumindo que expiresIn está em segundos
      Cookies.set(TOKEN_EXPIRATION_COOKIE, expirationTime.toString(), cookieOptions);

      return true;
    } catch (error) {
      console.error('Login error:', error);
      return false;
    }
  },

  logout: () => {
    Cookies.remove(TOKEN_COOKIE);
    Cookies.remove(TOKEN_TYPE_COOKIE);
    Cookies.remove(TOKEN_EXPIRATION_COOKIE);
    Cookies.remove(REMEMBER_ME_COOKIE);
    Cookies.remove(SAVED_EMAIL_COOKIE);
  },

  getToken: () => {
    return Cookies.get(TOKEN_COOKIE);
  },

  getTokenType: () => {
    return Cookies.get(TOKEN_TYPE_COOKIE);
  },

  getSavedEmail: (): string | undefined => {
    return Cookies.get(SAVED_EMAIL_COOKIE);
  },

  isRememberMeEnabled: (): boolean => {
    return Cookies.get(REMEMBER_ME_COOKIE) === 'true';
  },

  hasSavedEmail: (): boolean => {
    return !!Cookies.get(SAVED_EMAIL_COOKIE);
  },

  clearSavedEmail: () => {
    Cookies.remove(SAVED_EMAIL_COOKIE);
    Cookies.remove(REMEMBER_ME_COOKIE);
  },

  isAuthenticated: () => {
    const token = Cookies.get(TOKEN_COOKIE);
    const expiration = Cookies.get(TOKEN_EXPIRATION_COOKIE);
    
    if (!token || !expiration) {
      return false;
    }
    
    return new Date().getTime() < parseInt(expiration);
  },

  requireAuth: (redirectToLogin = true) => {
    const isAuthenticated = authService.isAuthenticated();
    
    if (!isAuthenticated && redirectToLogin) {
      if (typeof window !== 'undefined') {
        window.location.href = '/login';
      }
      return false;
    }
    return isAuthenticated;
  }
};