# 基于nginx，把dist,nginx.conf复制进去，设定启动命令nginx -g daemon off;
#1.选地基：基于nginx stable-alpine版本
FROM nginx:stable-alpine
#2.把jenkins刚刚构建好的dist复制进容器
COPY dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
