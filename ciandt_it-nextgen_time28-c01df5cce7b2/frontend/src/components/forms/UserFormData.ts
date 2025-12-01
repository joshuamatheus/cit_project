import { EditUserDTO } from "@/DTOs/user/EditUserDTO";

export const userTypes = [
    { id: 'COLLABORATOR', name: 'Colaborador' },
    { id: 'PDM', name: 'PDM' },
];

export const roles = [
    { id: 'PRODUCT_DESIGNER', name: 'Product Designer' },
    { id: 'UX_DESIGNER', name: 'Ux Designer' },
    { id: 'DEVELOPER', name: 'Developer' },
    { id: 'BUSINESS_PARTNER', name: 'Business Partner' },
    { id: 'TALENT_ACQUISITION', name: 'Talent Acquisition' },
];

export const positionMaps = [
    { id: 'INTERN', name: 'Intern' },
    { id: 'JUNIOR', name: 'Junior' },
    { id: 'MID_LEVEL', name: 'Mid-Level' },
    { id: 'SENIOR', name: 'Senior' },
    { id: 'MANAGER_MASTER', name: 'Manager/Master' },
    { id: 'SR_MANAGER_MASTER_2', name: 'Sr Manager/Master 2' },
];
export interface IUserForm{
  mode: 'create' | 'edit';
  userData?: EditUserDTO;
  onSubmit: (formData: Omit<EditUserDTO, 'id'>) => void;
}
