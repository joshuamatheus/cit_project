import Cookies from 'js-cookie';

const baseUrl: string = "http://localhost"; // process.env.BASE_URL
interface ErrorResponse {
  message: string;
  status: number;
  errors?: { [key: string]: string };
}
export async function fetchWrapper<T>(
  endpoint: string,
  options: RequestInit = { 
    credentials: "include", 
    headers: { "Content-Type": "application/json" } 
  } 
): Promise<T> {
  console.log(`Fetching from: ${baseUrl}${endpoint}`);
  console.log('Options:', options);
  // Recupera o token do cookie
  const token = Cookies.get('token'); 
  // Adiciona o cabeçalho Authorization se o token estiver presente
  if (token) {
    options.headers = {
      ...options.headers,
      'Authorization': `Bearer ${token}`,
    };
  }
  try {
    const response = await fetch(`${baseUrl}${endpoint}`, options);
    
    console.log('Response status:', response.status);
    
    if (response.status === 401) {
      return {} as T;
    }
    
    if (!response.ok) {
      let errorMessage: string;
      try {
        // Obter o texto da resposta primeiro
        const text = await response.text();
        
        // Se houver texto, tentar analisar como JSON
        if (text) {
          const errorResponse = JSON.parse(text);
          if (errorResponse.errors) {
            errorMessage = Object.values(errorResponse.errors).join('. ');
          } else {
            errorMessage = errorResponse.message || `HTTP error: ${response.status}`;
          }
        } else {
          errorMessage = `HTTP error: ${response.status}`;
        }
      } catch (parseError) {
        errorMessage = `HTTP error: ${response.status}`;
      }
      
      throw new Error(errorMessage);
    }
    
    // Tratamento especial para respostas sem conteúdo
    if (response.status === 204 || response.status === 201) {
      // Para APIs que retornam 204 No Content ou 201 Created sem corpo
      return { success: true } as unknown as T;
    }
    
    // Verificar se há conteúdo antes de tentar parsear como JSON
    const text = await response.text();
    if (!text || text.trim() === '') {
      return { success: true } as unknown as T;
    }
    
    const data = JSON.parse(text);
    return data;
  } catch (error) {
    console.error('Fetch error:', error);
    throw error;
  }
}