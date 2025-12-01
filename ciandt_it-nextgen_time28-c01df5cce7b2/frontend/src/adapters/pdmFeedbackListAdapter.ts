import { PdmFeedbackListDTO } from "@/DTOs/feedbackRequest/PdmFeedbackListDTO";
import { formatDate } from '@/utils/formatDate';
import { mapRole, mapPositionMap} from '@/utils/enumMappings';

// Interface para o formato mapeado para exibição
export interface DisplayPdmFeedback {
  id: number;
  login: string;
  role: string;
  positionMap: string;
  createdAt: string;
  status: string;
}

export class FeedbackAdapter {
  static toDisplayModel(feedbacks: PdmFeedbackListDTO[]): DisplayPdmFeedback[] {
    return feedbacks.map(feedback => ({
      id: feedback.requester.id,
      login: feedback.requester.email,
      role: mapRole(feedback.requester.role),
      positionMap: mapPositionMap(feedback.requester.positionMap),
      createdAt: formatDate(feedback.createdAt),
      status: feedback.status
    }));
  }

  // Método de conveniência para filtrar feedbacks pendentes
  static getPendingFeedbacks(displayFeedbacks: DisplayPdmFeedback[]): DisplayPdmFeedback[] {
    return displayFeedbacks.filter(feedback => 
      feedback.status === 'Não avaliado' || 
      feedback.status === 'Aguardando respostas' ||
      feedback.status === 'Em aprovação'
    );
  }

  // Método de conveniência para filtrar feedbacks concluídos
  static getCompletedFeedbacks(displayFeedbacks: DisplayPdmFeedback[]): DisplayPdmFeedback[] {
    return displayFeedbacks.filter(feedback => 
      feedback.status === 'Finalizado'
    );
  }
}