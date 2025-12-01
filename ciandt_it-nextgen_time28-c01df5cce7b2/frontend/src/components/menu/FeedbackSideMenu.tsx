import Box from "@mui/material/Box";
import Divider from "@mui/material/Divider";
import StepCard from "../boxes/StepCard";
import ProfileCard from "../boxes/ProfileCard";
import { UserProfileDTO } from "@/DTOs/user/UserProfileDTO";
import { StepStatus } from "../../../.next/types/stepStatus";
import Typography from "@mui/material/Typography";

const mockUser: UserProfileDTO = {
    name: 'João Silva',
    email: 'joao.silva@example.com',
    role: 'Desenvolvedor',
    positionMap: 'Frontend Developer',
  };
  
const steps: {name: string; status: StepStatus}[] = [
    { name: "Configuração do formulário", status: "inProgress" },
    { name: "Aprovação PDM", status: "toDo" },
    { name: "Coleta de feedback", status: "toDo" },
    { name: "Análise de Resultados", status: "toDo" },
];  

interface FeedbackSideMenuProps {
    userProfile?: UserProfileDTO,
    stepStatus?: StepStatus
}

const FeedbackSideMenu: React.FC<FeedbackSideMenuProps> = ({userProfile, stepStatus }) => {
    return (
        <Box
          sx={{
            height: '80%',
            width:'70%',
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          <ProfileCard user={mockUser}></ProfileCard>

          <Divider sx={{ my: 2 }} />
          
          <Typography variant="h6">Coleta de Feedback</Typography>
          {steps.map((step, index) => (
            <StepCard 
              key={index}
              currentStep={index + 1}
              totalSteps={steps.length}
              stepName={step.name}
              status={step.status}
            />
          ))}

          <Divider sx={{ my: 1 }} />
          <Divider sx={{ my: 1 }} />

        </Box>
    )
}

export default FeedbackSideMenu;