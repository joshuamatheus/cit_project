"use client";

import { useState } from "react";
import { Button, Menu, MenuItem } from "@mui/material";
import LanguageIcon from "@mui/icons-material/Language";
import theme from "@/theme";

export default function LanguageMenu() {
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const handleMenuClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    <>
      <Button
        variant="contained"
        startIcon={<LanguageIcon />}
        onClick={handleMenuClick}
        sx={{
          backgroundColor: theme.palette.buttons.pink,
          color: theme.palette.text.secondary,
          borderRadius: "8px",
          textTransform: "none",
          "&:hover": { backgroundColor: theme.palette.buttons.darkPink },
        }}
      >
        PT-BR
      </Button>

      {/* Dropdown Menu */}
      <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleClose}>
        <MenuItem onClick={handleClose}>EN-US</MenuItem>
        <MenuItem onClick={handleClose}>ES-ES</MenuItem>
      </Menu>
    </>
  );
}