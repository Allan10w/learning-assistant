# 基于nginx，把dist,nginx.conf复制进去，设定启动命令nginx -g daemon off;
FROM nginx:stable-alpine
COPY dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
