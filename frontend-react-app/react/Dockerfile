FROM node:20.12.0-buster
WORKDIR /workspace/frontend

COPY . .

RUN npm install
ENTRYPOINT ["npm","run","dev"]