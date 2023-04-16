FROM node:14-alpine

# 创建应用目录并设置工作目录
WORKDIR /app

# 复制应用源代码到容器中
COPY . .

# 安装应用依赖
RUN npm install --production

# 启动应用
CMD [ "node", "index.js" ]
