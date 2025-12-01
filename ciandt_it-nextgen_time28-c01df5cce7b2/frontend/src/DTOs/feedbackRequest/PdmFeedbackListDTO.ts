export interface UserInfoDTO {
  id: number;
  email: string;
  role: string;
  positionMap: string;
}
export interface PdmFeedbackListDTO {
  requester: UserInfoDTO;
  status: string;
  createdAt: string;
}