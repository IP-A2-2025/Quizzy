#!/bin/bash
cd ~/quizzy-app || exit 1

cat > .env <<EOF
VITE_BACKEND_URL=http://52.28.16.232
SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
GEMINI_API_KEY=${GEMINI_API_KEY}
EOF

if ! command -v docker &> /dev/null; then
  sudo dnf update -y
  sudo dnf install -y docker
  sudo systemctl start docker
  sudo systemctl enable docker
  sudo usermod -aG docker ec2-user
  newgrp docker
fi

if ! docker compose version &> /dev/null; then
  mkdir -p ~/.docker/cli-plugins
  curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) \
    -o ~/.docker/cli-plugins/docker-compose
  chmod +x ~/.docker/cli-plugins/docker-compose
fi

sudo yum install -y amazon-ecr-credential-helper

mkdir -p ~/.docker
cat > ~/.docker/config.json << EOF
{
  "credHelpers": {
    "public.ecr.aws": "ecr-login"
  }
}
EOF

docker compose down || true
docker compose pull
docker compose up -d