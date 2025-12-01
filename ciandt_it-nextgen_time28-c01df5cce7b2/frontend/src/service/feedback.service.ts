import { FeedbackListDTO } from '@/DTOs/feedbackRequest/FeedbackListDTO';
import { PdmFeedbackListDTO } from '@/DTOs/feedbackRequest/PdmFeedbackListDTO';
import { fetchWrapper } from '@/utils/fetchApi';
import Cookies from 'js-cookie';
import { TOKEN_COOKIE } from './auth.service';
import { CreateFeedbackRequestDTO } from '@/DTOs/feedbackRequest/CreateFeedbackRequestDTO';

export const getAllFeedbacks = async (): Promise<FeedbackListDTO[]> => {
  try {
    return await fetchWrapper<FeedbackListDTO[]>('/requests/feedbacklist');
  } catch (error) {
    console.error('Error fetching feedbacks:', error);
    throw error;
  }
};

export const reviewFeedbackRequest = async (
  id: FeedbackRequestDTO['id'],
  approved: boolean,
  rejectMessage?: string
): Promise<FeedbackRequestDTO> => {
  try {

    const token = Cookies.get(TOKEN_COOKIE)

    return await fetchWrapper<FeedbackRequestDTO>(`/requests/${id}/review`, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        approved,
        rejectMessage
      })
    });
  } catch (error) {
    console.error('Error submitting feedback review:', error);
    throw error;
  }
};

export const getFeedbackRequestByID = async (id: FeedbackRequestDTO['id']): Promise<FeedbackRequestDTO> => {
  try {
    const token = Cookies.get(TOKEN_COOKIE);;
    
    return await fetchWrapper<FeedbackRequestDTO>(`/requests/form/${id}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
  } catch (error) {
    console.error('Error fetching feedbacks:', error);
    throw error;
  }
};

export const deleteFeedback = async (id: FeedbackListDTO['id']): Promise<void> => {
  try {
    const response = await fetch('http://localhost/requests/feedbacklist', {
      method: 'DELETE',
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
  } catch (error) {
    console.error('Error deleting feedback:', error);
    throw error;
  }
};

export const getPdmFeedbackList = async (): Promise<PdmFeedbackListDTO[]> => {
  try {
    return await fetchWrapper<PdmFeedbackListDTO[]>('/requests/pdm/feedbacklist', {
      method: 'GET'
    });
  } catch (error) {
    console.error('Error fetching PDM feedbacks:', error);
    throw error;
  }
};

export const createFeedbackRequest = async (data: CreateFeedbackRequestDTO): Promise<FeedbackRequestDTO> => {
  try {
    const token = Cookies.get(TOKEN_COOKIE);

    return await fetchWrapper<FeedbackRequestDTO>('/requests/register', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
  } catch (error) {
    console.error('Error creating feedback request:', error);
    throw error;
  }
};