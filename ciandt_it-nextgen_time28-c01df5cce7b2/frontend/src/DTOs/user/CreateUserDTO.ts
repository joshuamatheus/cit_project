export interface CreateUserDTO {
  name: string;
  email: string;
  type: string;
  pdmEmail?: string;
  role: string;
  positionMap: string;
}