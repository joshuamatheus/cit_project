FROM node:18-alpine AS builder

ARG APP_NAME=api_gateway

WORKDIR /app

COPY . .

RUN cd ${APP_NAME} && npm install && npm run build

FROM node:18-alpine

ARG APP_NAME=api_gateway

COPY --from=builder /app/${APP_NAME} .

CMD [ "npm", "run", "start:prod" ]