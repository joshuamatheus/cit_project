import { AppBar, Toolbar, Typography, IconButton, Avatar } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import LanguageMenu from "../menu/menu";

export default function Header() {
  return (
    <AppBar position="static" sx={{ backgroundColor: "white", boxShadow: "none", padding: "10px 20px" }}>
      <Toolbar sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        {/* Logo */}
        <Typography variant="h6" sx={{ fontWeight: "bold", color: "#9B4274" }}>
          CI&T <span style={{ fontWeight: "400" }}>JOURNEY</span>
        </Typography>

        <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
          {/* Profile Avatar */}
          <Avatar alt="User Profile" src="https://via.placeholder.com/40" />

          {/* Language Selector (Now a Client Component) */}
          <LanguageMenu />

          {/* Hamburger Menu */}
          <IconButton>
            <MenuIcon sx={{ color: "#9B4274" }} />
          </IconButton>
        </div>
      </Toolbar>
    </AppBar>
  );
}
