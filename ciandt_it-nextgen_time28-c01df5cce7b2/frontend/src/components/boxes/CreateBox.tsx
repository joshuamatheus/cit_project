import React from "react";
import { Box, Typography } from "@mui/material";
import MenuBox from "./MenuBox";
import { ActionButton } from "../buttons/ActionButton";

interface ICreateBox {
  line1: string;
  line2: string;
  onCreateUser: () => void;
}

export const CreateBox: React.FC<ICreateBox> = ({ line1, line2, onCreateUser }) => {
  
  return (
    <MenuBox>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h6">
            {line1}
          </Typography>
          <Typography>
            {line2}
          </Typography>
        </Box>
        <ActionButton label='Criar' backgroundColor="blue" onClick={onCreateUser} />
      </Box>
    </MenuBox>
  );
};
