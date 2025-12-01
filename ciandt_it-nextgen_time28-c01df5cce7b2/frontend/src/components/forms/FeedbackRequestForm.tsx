import { Box, Divider, Typography } from "@mui/material";
import MailOutlineIcon from '@mui/icons-material/MailOutline';
import { ActionButton } from "../buttons/ActionButton";
import InvitesForm from "./InvitesForm";
import QuestionsForm from "./QuestionsForm";
import theme from "@/theme";
import MenuBox from "../boxes/MenuBox";
import StepTitleCard from "../boxes/StepTitleCard";
import { createFeedbackRequest } from '@/service/feedback.service';
import { CreateFeedbackRequestDTO } from '@/DTOs/feedbackRequest/CreateFeedbackRequestDTO';
import { useCallback, useState } from "react";
import { useRouter } from 'next/navigation';
interface FeedbackRequestFormProps {
    
}

const FeedbackRequestForm: React.FC<FeedbackRequestFormProps> = ({ }) => {
  const [questions, setQuestions] = useState<string[]>([]);
  const [appraiserEmails, setAppraiserEmails] = useState<string[]>([]);
  const router = useRouter()

  const onSave = () => {

  };

  const onSubmit = async () => {
    try {
      const data: CreateFeedbackRequestDTO = {
        questions: questions,
        appraiserEmails: appraiserEmails,
      };

      const feedbackRequest = await createFeedbackRequest(data);
      console.log("Feedback request response:", feedbackRequest);

      alert('Feedback request submitted successfully!');

      router.push('/collaborator');
      
    } catch (error) {
      console.error('Error submitting feedback request:', error);
      alert('Failed to submit feedback request.');
    }
  };

  
  const handleQuestionsChange = useCallback((newQuestions: { id: number; text: string }[]) => {
    const questionTexts = newQuestions.map(question => question.text);
    setQuestions(questionTexts);
  }, []);

  const handleAppraiserEmailsChange = useCallback((newInvites: { id: string; email: string; status: string }[]) => {
    const emailList = newInvites.map(invite => invite.email);
    setAppraiserEmails(emailList);
  }, []);

  return (
      <MenuBox>
        <Box
          sx={{
            backgroundColor: theme.palette.background.paper,
            display: 'flex',
            flexDirection: 'column',
            padding: '10px',
          }}
        >
          <StepTitleCard stepNumber={1} stepTitle="Formulário"/>

          <Divider sx={{ my: 2 }} />

          <Typography variant="subtitle2">Perguntas personalizadas* (Mín. 1, máx. 3)</Typography>
          <Typography sx={{my:2}} variant="body2">
          Para além das perguntas do formulário padrão de avaliação, inclua de 1 a 3 perguntas personalizadas para atender às necessidades específicas da sua avaliação.
          <br/>(Ex.: Como você avalia minha entrega técnica do último ano?)
          </Typography>
          
          <QuestionsForm onQuestionsChange={handleQuestionsChange}/>

          <Divider sx={{ my: 2 }} />

          <Box>
            <Typography variant="subtitle2">Pessoas convidadas* (Mín. 2, máx. 9)</Typography>
            <Typography sx={{my:2}} variant="body2">
            Convide pessoas que possam oferecer insights relevantes para o seu Journey Check-in. Alinhe com seu PDM quem são as mais pertinentes para essa etapa.              </Typography>
            <InvitesForm  onInvitesChange={handleAppraiserEmailsChange}/>
          </Box>
        </Box>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            width: '100%',
            my: 1,
          }}
        >
          <ActionButton label={'Salvar'} backgroundColor='blue' onClick={onSave} />
          <ActionButton label={'Enviar'} icon={<MailOutlineIcon/>} backgroundColor='blue' onClick={onSubmit} />
        </Box>
        <Divider sx={{ my: 2 }} />
      </MenuBox>
  );
};

export default FeedbackRequestForm;
