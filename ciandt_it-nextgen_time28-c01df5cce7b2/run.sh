#!/bin/bash

# Configurando variáveis de ambiente
echo "Configurando variáveis de ambiente..."
export DB_HOST=localhost
export DB_PORT=5432
export DB_USER=postgres
export DB_PASS=root
export JWT_SECRET_KEY="4Z^XrroxR@dWxqf\$!@#qGr4P"
export JWT_ISSUER="user-api"
export DATABASE_URL="postgresql://postgres:root@localhost:5432/feedback"

pkill -f com.ciandt
pkill -f uvicorn
pkill -f npm

# Função para rodar serviços e logar a saída
run_service() {
  local name="$1"
  local cmd="$2"
  echo "Iniciando $name..."
  # Redireciona stdout e stderr para um arquivo de log e deixa o processo rodando em background
  nohup bash -c "$cmd" > "logs/${name}.log" 2>&1 &
}

if docker ps -a --format '{{.Names}}' | grep -wq "pgfeedback"; then
  # Container existe; vamos verificar se ele está rodando
  status=$(docker inspect -f '{{.State.Status}}' pgfeedback)
  echo "Status atual do container pgfeedback: $status"

  if [ "$status" != "running" ]; then
    echo "Iniciando o container pgfeedback..."
    docker start pgfeedback
    echo "Container pgfeedback iniciado."
  else
    echo "Container pgfeedback já está rodando."
  fi
else
  # Container não existe, cria-lo
  docker run -d --name pgfeedback -e POSTGRES_PASSWORD=root -e POSTGRES_DB=feedback -p 5432:5432 postgres 
fi


# 1. Buildar e iniciar os microserviços Java
echo "Buildando os projetos Java..."
cd java_backend || exit 1
mvn clean install -DskipTests
cd - || exit 1

# Iniciando os microserviços Java
run_service "usermanagement" "cd java_backend && mvn -pl microservices/usermanagement spring-boot:run"
run_service "feedbackrequest" "cd java_backend && mvn -pl microservices/feedbackrequest spring-boot:run"
run_service "feedbackresponse" "cd java_backend && mvn -pl microservices/feedbackresponse spring-boot:run"

# 2. Iniciar o microserviço Python (Feedback Response View)
run_service "python_service" "cd python_backend/feedbackresponseview && poetry install && poetry run uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload"

# 3. Iniciar o API Gateway (Node.js)
run_service "api_gateway" "cd node_backend/api_gateway && npm install && npm run start"

# 4. Iniciar o Nginx
echo "Iniciando o Nginx..."
# Se o Nginx já estiver configurado corretamente e instalado, usamos:
sudo cp gateway/nginx.conf /etc/nginx/sites-enabled/default
sudo sed -Ei 's/(server\s+)[^:]+(:[0-9]+;)/\1localhost\2/g' /etc/nginx/sites-enabled/default
sudo service nginx restart

echo "Todos os serviços foram iniciados."
echo "Logs gerados em: usermanagement.log, feedbackrequest.log, feedbackresponse.log, python_service.log, api_gateway.log, nginx.log"
echo "Para terminar todos os processos, use o comando 'kill' ou 'pkill -f <nome_do_servico>'."
# Aguardar para manter o script em execução (opcional)
wait