import express, { NextFunction, Request, Response } from 'express';
import { createProxyMiddleware } from 'http-proxy-middleware';
import { OnProxyEvent } from 'http-proxy-middleware/dist/types';
import jwt from 'jsonwebtoken';

const app = express();

// Chave secreta para verificar o JWT (use uma chave mais segura em produção)
const SECRET_KEY = process.env.JWT_SECRET_KEY;
const ISSUER = process.env.JWT_ISSUER;

if (!SECRET_KEY || !ISSUER)
    throw new Error("No scret key or issuer for JWT on environment!");
    

// middleware global que verifica a validade do token jwt
app.use((req: Request, res: Response, next: NextFunction) => {
    const token = req.headers['authorization']?.split(' ')[1]; // Pega o token do header

    if (!token) {
        return res.status(403).send({ message: 'Token não fornecido!' });
    }

    jwt.verify(token, SECRET_KEY, {issuer: ISSUER}, (err: any, decoded: any) => {
        if (err) {
            return res.status(401).send({ message: 'Token inválido!' });
        }
        
        // Adiciona o payload do token ao header 'claims'
        req.headers['x-user'] = JSON.stringify(decoded['user']); // Converte o payload para string

        next(); // Token válido, continue para a próxima função/middleware
    });
});

const proxyEvents: OnProxyEvent = {
    proxyReq: (proxyReq, req, res) => {
        proxyReq.setHeader("x-user", req.headers['x-user'] || "")
    }
}

// Proxy para o serviço de gerenciamento de usuários
app.use('/users', createProxyMiddleware({
    method: 'GET',
    target: process.env.USER_MANAGEMENT_API || 'http://usermanagement:8080',
    changeOrigin: true,
    // pathRewrite: {
    //     '^/users': '', // Remove '/users' da URL antes de passar para o backend
    // },
    on: proxyEvents,
    logger: console
}));


export default app;