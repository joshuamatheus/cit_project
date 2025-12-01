import request from 'supertest';
import express from 'express';
import dotenv from 'dotenv';
import jwt from 'jsonwebtoken';
import * as http from "http";

dotenv.config({path: '.env.test'});

import app from './app';

let server: http.Server;

describe('Proxy Middleware Tests', () => {
    beforeAll((done) => {
        // Iniciar o servidor proxy
        server = app.listen(9999, () => {
            console.log(`Proxy server is running on http://localhost:${9999}`);
            done();
        });
    });

    afterAll((done) => {
        // Fechar o servidor após os testes
        server.close(done);
    });

    // Criar o servidor de gerenciamento de usuários
    let userManagementServer: any;

    beforeAll((done) => {
        const userManagementApp = express();

        userManagementApp.use(express.json());

        userManagementApp.get('/', (req, res) => {
            const xUser = req.headers['x-user'] as string;

            res.json({
                message: 'Usuário encontrado!',
                user: JSON.parse(xUser)
            });
        });

        userManagementServer = userManagementApp.listen(8080, () => {
            console.log(`User Management API is running on http://localhost:8080`);
            done();
        });
    });

    afterAll((done) => {
        // Fechar o servidor de gerenciamento de usuários após os testes
        userManagementServer.close(done);
    });

    it('should return 401 for invalid token', async () => {
        const response = await request(app)
            .get('/users')
            .set('Authorization', 'Bearer invalidtoken');

        expect(response.status).toBe(401);
        expect(response.body.message).toBe('Token inválido!');
    });

    it('should return 403 for missing token', async () => {
        const response = await request(app).get('/users');

        expect(response.status).toBe(403);
        expect(response.body.message).toBe('Token não fornecido!');
    });

    it('should pass valid token and add x-user header', async () => {
        const user = { user: { id: 1, name: 'John Doe' } };
        const token = jwt.sign(user, process.env.JWT_SECRET_KEY || '', {issuer: process.env.JWT_ISSUER || ''});

        const response = await request(app)
            .get('/users')
            .set('Authorization', `Bearer ${token}`)

        expect(response.status).toBe(200); // Espera-se que o proxy retorne 200 OK
        expect(response.body.user).toEqual(user.user); // Confirma que o header x-user foi adicionado
    });
});