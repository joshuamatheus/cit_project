export const mapUserType = (type: string): string => {
    switch (type) {
      case 'PDM':
        return 'PDM';
      case 'COLLABORATOR':
        return 'Colaborador';
      case 'ADMIN':
        return 'ADMIN';
      default:
        return type;
    }
  };
  
  export const mapRole = (role: string): string => {
    switch (role) {
      case 'PRODUCT_DESIGNER':
        return 'Product Designer';
      case 'UX_DESIGNER':
        return 'Ux Designer';
      case 'DEVELOPER':
        return 'Developer';
      case 'BUSINESS_PARTNER':
        return 'Business Partner';
      case 'TALENT_ACQUISITION':
        return 'Talent Acquisition';
      default:
        return role;
    }
  };

  export const mapPositionMap = (position: string): string => {
    switch (position) {
      case 'INTERN':
        return 'Intern';
      case 'JUNIOR':
        return 'Junior';
      case 'Mid-Level':
        return 'Pleno';
      case 'SENIOR':
        return 'Senior';
      case 'MANAGER_MASTER':
        return 'Manager/Master';
      case 'SR_MANAGER_MASTER_2':
        return 'Sr Manager/Master 2';
      default:
        return position;
    }
  };