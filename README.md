# 🏙️ AdelifeHub

AdelifeHub is a local lifestyle information platform for students and residents in Adelaide, covering housing rentals, job hunting, second-hand trading, and campus social posts.  
阿德生活通是一个面向阿德莱德留学生和本地居民的生活信息平台，提供租房、找工、二手交易和校园墙互动功能。  

Built with **Spring Boot + MySQL + React**, featuring JWT authentication and RESTful APIs.  
基于 **Spring Boot + MySQL + React** 开发，采用 JWT 身份认证与 RESTful API 架构。  

---

## ✨ Features 功能

### 👤 User System 用户系统
- Register / Login / JWT authentication  
- View & update profile  
- Avatar upload  
- 注册 / 登录 / JWT 身份认证  
- 个人资料查看与修改  
- 用户头像上传  

### 🏠 Listings 租房 / 找工 / 二手交易
- Create, edit, delete listings  
- Search & pagination  
- Favorite & report  
- 信息发布、编辑、删除  
- 支持条件搜索与分页展示  
- 收藏与举报功能  

### 💬 Comments 评论系统
- Comment on listings  
- Delete comments (author/admin)  
- 针对房源或二手信息评论  
- 评论删除（作者/管理员）  

### 📌 Campus Wall 校园墙
- Create posts, like, delete  
- Text & image supported  
- 自由发帖、点赞、删除  
- 支持文字与图片  

### 📷 File Upload 文件上传
- Upload images and get public URL  
- 图片上传并返回可访问链接  

---

## 🛠️ Tech Stack 技术栈
- **Backend**: Spring Boot, MyBatis, JWT  
- **Database**: MySQL  
- **Frontend**: React, Axios  
- **Others**: Swagger / OpenAPI  

---


### ▶ 🚀 Quick Start 快速开始

```bash
Backend 启动后端

cd backend
mvn spring-boot:run


Frontend 启动前端

cd frontend
npm install
npm start
