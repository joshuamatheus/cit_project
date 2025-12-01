import { NextRequest, NextResponse } from "next/server";
import { getCurrentUserType } from "./service/user.service";

const publicRoutes = [
  { path: "/register", whenAuthenticated: "redirect" },
  { path: "/login", whenAuthenticated: "redirect" },
];

const adminRoutes = [
  "/admin",
  "/admin/create",
  "/admin/edit"
];
const collaboratorRoutes = [
  "/collaborator",
  "/collaborator/createfeedback"
];
const pdmRoutes = ['/pdm'];

export async function middleware(req: NextRequest) {
  const token = req.cookies.get("token")?.value; // Obtém o token do cookie (se existir)
  const currentPath = req.nextUrl.pathname;

  // Verifica se a rota atual está na lista de rotas públicas
  const matchedRoute = publicRoutes.find((route) => route.path === currentPath);

  if (matchedRoute) {
    // Se a rota pública tem `whenAuthenticated: "redirect"` e o usuário está logado
    if (matchedRoute.whenAuthenticated === "redirect" && token) {
      return NextResponse.redirect(new URL("/", req.url)); // Redireciona para o dashboard
    }
    return NextResponse.next(); // Permite o acesso à rota pública
  }

  // Se não for uma rota pública e o usuário não está autenticado, redireciona para login
  if (!token) {
    return NextResponse.redirect(new URL("/login", req.url));
  }

  if (adminRoutes.some(route => currentPath.startsWith(route))) {
    // Obter o tipo de usuário
    const userType = await getCurrentUserType(token);

    // Se não for admin, redireciona para home
    if (userType.type !== 'ADMIN') {
      return NextResponse.redirect(new URL('/', req.url));
    }
  }

  if (collaboratorRoutes.some(route => currentPath.startsWith(route))) {
    const userType = await getCurrentUserType(token);

    if (userType.type !== 'COLLABORATOR') {
      return NextResponse.redirect(new URL('/', req.url));
    }
  }

  if (pdmRoutes.some(route => currentPath.startsWith(route))) {
    const userType = await getCurrentUserType(token);

    if (userType.type !== 'PDM') {
      return NextResponse.redirect(new URL('/', req.url));
    }
  }

  return NextResponse.next(); // Permite a requisição normalmente
}

// Configuração do middleware para **evitar execuções desnecessárias**
export const config = {
    matcher: [
      /*
       * Match all request paths except for the ones starting with:
       * - api (API routes)
       * - _next/static (static files)
       * - _next/image (image optimization files)
       * - favicon.ico, sitemap.xml, robots.txt (metadata files)
       */
      '/admin/:path*', '/collaborator/:path*', '/pdm/:path*', '/((?!api|_next/static|_next/image|favicon.ico|sitemap.xml|robots.txt).*)',
    ],
  }
