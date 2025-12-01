"use client";

import React, { useState, useEffect } from 'react';
import '@fortawesome/fontawesome-svg-core/styles.css'
import { faCircleUser, faLock } from '@fortawesome/free-solid-svg-icons';
import LoginPageTitle from '@/components/header/LoginPageTitle';
import TextInputField from '@/components/inputFields/TextInputField';
import PasswordInputField from '@/components/inputFields/PasswordInputField';
import RememberMeCheckbox from '@/components/boxes/RememberMeCheckbox';
import LinkButton from '@/components/buttons/LinkButton';
import LoginButton from '@/components/buttons/LoginButton';
import SubTitle from '@/components/header/LoginPageSubTitle';
import { authService } from '@/service/auth.service';
import { checkUserByEmail, getCurrentUserType, registerPassword } from '@/service/user.service';
import img from '@/components/assets/background-image.png';
import { useRouter } from 'next/navigation';

const LoginPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);
  const [isNewUser, setIsNewUser] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [userName, setUserName] = useState<string | null>(null);
  const [emailChecked, setEmailChecked] = useState(false);
  
  const router = useRouter();

  useEffect(() => {
    const loadSavedEmail = async () => {
      if (authService.hasSavedEmail()) {
        const savedEmail = authService.getSavedEmail();
        
        if (savedEmail) {
          setEmail(savedEmail);
          setRememberMe(authService.isRememberMeEnabled());
  
          await checkEmailAndProceed(savedEmail);
        }
      }
    };

    loadSavedEmail();
  }, []);

  const checkEmailAndProceed = async (emailToCheck: string) => {
    if (!emailToCheck) {
      setError('Email is required');
      return;
    }

    try {
      setIsLoading(true);
      const userData = await checkUserByEmail(emailToCheck);
      
      setUserName(userData.name);
      setIsNewUser(!userData.hasPassword);
      setEmailChecked(true);
    } catch (error) {
      console.error('Error checking email:', error);
      setError('User not found. Please check your email or contact support.');
      setIsNewUser(false);
      setEmailChecked(false);
    } finally {
      setIsLoading(false);
    }
  };

  const handleEmailSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await checkEmailAndProceed(email);
  };

  const handlePasswordSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!password) {
      setError('Password is required');
      return;
    }
    
    let loginSuccess: boolean = false;

    try {
      setIsLoading(true);

      if (isNewUser) {
        loginSuccess = (await handleNewUserRegistration()) as boolean;
      } else {
        loginSuccess = await handleExistingUserLogin();
      }
  
      if (loginSuccess) {
        const token = authService.getToken(); // Obtém o token do cookie

        if (token) {
          try {
            const userType = await getCurrentUserType(token); // Chame a função para obter o tipo de usuário

            switch (userType.type) {
              case 'ADMIN':
                router.push('/admin');
                break;
              case 'COLLABORATOR':
                router.push('/collaborator');
                break;
              case 'PDM':
                router.push('/pdm');
                break;
              default:
                router.push('/');
                break;
            }
          } catch (error) {
            console.error('Error getting user type:', error);
            setError('Failed to determine user role. Redirecting to home.');
            router.push('/');
          }
        } else {
          setError('Token not found after login.');
          router.push('/login');
        }
      } else {
        setError('Login failed. Please try again.');
      }
    } catch (error: any) {
      console.error('Login failed:', error);
      setError(error.message || 'Login failed. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleNewUserRegistration = async (): Promise<boolean> => {
    if (!confirmPassword) {
      setError('Confirm password is required');
      return false;
    }

    if (password !== confirmPassword) {
      setError("Passwords don't match!");
      return false;
    }

    try {
      setIsLoading(true);
      await registerPassword(email, password, confirmPassword);
      
      const loginSuccess = await authService.login(email, password, rememberMe);
      if (loginSuccess) {
        return true;
      } else {
        setError('Login failed after password registration. Please try logging in again.');
        return false;
      }
    } catch (error: any) {
      console.error('Registration failed:', error);
      setError(error.message && error.message.includes('Password must') 
        ? error.message 
        : 'Failed to register password. Please try again.'
      );
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const handleExistingUserLogin = async (): Promise<boolean> => {
    try {
      setIsLoading(true);
      const loginSuccess = await authService.login(email, password, rememberMe);
      
      if (loginSuccess) {
        return true;
      } else {
        setError('Invalid email or password. Please try again.');
        return false;
      }
    } catch (error: any) {
      console.error('Login failed:', error);
      setError('Invalid email or password. Please try again.');
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const handleBackToEmail = () => {
    setEmailChecked(false);
    setIsNewUser(false);
    setPassword('');
    setConfirmPassword('');
    setError(null);
  };

  const renderEmailForm = () => (
    <form onSubmit={handleEmailSubmit}>
      <TextInputField
        icon={faCircleUser}
        placeholder="Email"
        type="email"
        value={email}
        marginBottom='20px'
        onChange={(e) => setEmail(e.target.value)}
      />
      <div className="mt-10">
      <LoginButton label='Login'type="submit"/>
      </div>
    </form>
  );

  const renderPasswordForm = () => (
    <form onSubmit={handlePasswordSubmit}>
      <TextInputField
        icon={faCircleUser}
        placeholder="Email"
        type="email"
        value={email}
        marginBottom='20px'
        onChange={() => {}} 
      />
      
      <PasswordInputField
        icon={faLock}
        placeholder="Password"
        type="password"
        value={password}
        marginBottom='16px'
        showPasswordToggle={true}
        onChange={(e) => setPassword(e.target.value)}
      />
      
      {isNewUser && (
        <PasswordInputField
          icon={faLock}
          placeholder="Confirm Password"
          type="password"
          marginBottom='16px'
          value={confirmPassword}
          showPasswordToggle={true}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
      )}
      
      <div className="flex justify-between items-center" style={{marginBottom: '32px'}}>
        {!isNewUser && (
          <>
            <RememberMeCheckbox
              checked={rememberMe}
              onChange={(e) => setRememberMe(e.target.checked)}
            />
            <LinkButton label='Forgot Password'/>
          </>
        )}
        {isNewUser && (
          <div className="text-xs text-white/80">
            Password must contain at least 8 characters, including uppercase, lowercase, number, and special character.
          </div>
        )}
      </div>
      
      <div className="flex flex-col gap-2">
      <LoginButton label='Login'type="submit"/>
        
        <button 
          type="button" 
          onClick={handleBackToEmail}
          className="text-sm text-white/80 underline py-2"
        >
          Go back
        </button>
      </div>
    </form>
  );

  return (
    <div className="flex h-screen">
      <div className="w-47/100 bg-[#690037] p-12 flex items-center justify-center">
        <div className="w-2/3 h-[419px] text-white">
          <LoginPageTitle text={userName ? `Welcome, ${userName}!` : "Welcome!"}/>
        
          <SubTitle text={
            emailChecked 
              ? (isNewUser ? "Please create your password" : "Please enter your password") 
              : "Please enter your email"
          } />
          
          {error && (
            <div className="bg-red-600 text-white p-2 rounded mb-4 text-sm">
              {error}
            </div>
          )}
          
          {!emailChecked ? renderEmailForm() : renderPasswordForm()}
        </div>
      </div>
      <div className="w-53/100 bg-gray-100">
        <img src={img.src} alt="" className="w-full h-full object-cover object-center w-full h-full max-w-full max-h-full"/>
      </div>
    </div>
  );
};

export default LoginPage;