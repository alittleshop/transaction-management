# 基于 Node.js v20.18.3 镜像作为构建环境
FROM node:20.18.3-alpine as build

# 设置工作目录
WORKDIR /app

# 将 package.json 和 package-lock.json 复制到工作目录
COPY package*.json ./

# 安装依赖
RUN npm install

# 将所有文件复制到工作目录
COPY . .

# 构建项目
RUN npm run build

# 使用 Nginx 作为生产环境的基础镜像
FROM nginx:1.21.0-alpine

# 将前端构建后的文件复制到 Nginx 的默认静态文件目录
COPY --from=build /app/build /usr/share/nginx/html

# 暴露 80 端口
EXPOSE 80

# 启动 Nginx 服务
CMD ["nginx", "-g", "daemon off;"]