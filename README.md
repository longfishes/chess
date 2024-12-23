# chess - 中国象棋联机版

这是一个来自某学校的 面向对象实践 / Java 课设，要求是使用原生Java（Swing，Socket，nio）模仿QQ游戏，实现联机版中国象棋，主要包括登录、联机大厅和房间三个模块

本人在去年五子棋的开发经验下，其中在主要架构设计完成之后，借助最近有点热度的Cursor编辑器的AI完成机械的代码书写加快了流程，并花费2.5天完成

![](https://static.longfish.site/passage/img/chess-preview.png)

- 服务端运行

server包下Main主函数

- 客户端运行

client包下Main主函数



## 大厅主要功能

1. 登录
2. 自定义用户名
3. 选择头像
4. 加入联机大厅
5. 加入房间（坐下）   
6. 服务端公告
5. 聊天公屏

## 房间主要功能

1. 下棋
2. 悔棋
3. 求和
4. 认输
5. 打字聊天

## 棋盘主要功能

1. 选择提示
2. 落子提示
3. 下棋方提示   
4. 音效（走，吃，将）

## 服务端端口信息

为了ServerSocket之间不冲突，为服务端的每个socket绑定唯一端口

| 类名           | 名称       | 端口               |
| -------------- | ---------- | ------------------ |
| MainServer     | 主服务器   | 11111              |
| ServerInstance | 房间服务器 | (1-15) 11112-11126 |
| ChatServer     | 聊天服务器 | (1-15) 11212-11226 |

## 网络通信方式

- TCP

- 服务端模式：nio

说到这里的要求真是爆炸，由于实践要求使用非阻塞式io做服务器性能优化，使我不得不重构原来的主服务器MainServer，server包下的MainServerNio是重构之后的服务端，结果就是反向优化，各种粘包半包问题，发送格式不一致等等，一条消息可能发不完或者两条消息被连读，总之在我的缝补之下可以勉强代替原版的MainServer支撑一下

性能优化？为了那15个房间顶多30个用户去做所谓的高并发，最后带来各种问题。目前版本的MainServerNio还可以正常运行

原来就觉得像netty这种nio框架比较底层就没学，实际项目开发用到的地方寥寥无几，像websocket等技术更是nio的高度封装。这下好了，直接快进到netty源码

## 房间服务端指令数据传输

由于在正常情况下，房间服务器实例传输的数据应为四个整数，分别记录来时的横纵坐标，但是房间要求实现悔棋，求和，以及认输功能。因此设计负数作为指令传输

| 指令名称 | 指令格式    |
| -------- | ----------- |
| 开始     | -1 -1 -1 -1 |
| 求和     | -1 -2 -2 -2 |
| 同意求和 | -1 -2 -1 -2 |
| 拒绝求和 | -1 -2 -2 -1 |
| 悔棋     | -1 -3 -3 -3 |
| 同意悔棋 | -1 -3 -1 -3 |
| 拒绝悔棋 | -1 -3 -3 -1 |
| 认输     | -1 -4 -4 -4 |

## 主服务器数据指令数据传输

主服务器的作用是处理各个用户的登录，加入房间，离开等指令，以及向客户端发送有人加入，有人退出等指令

- 客户端指令

| 指令名称 | 指令格式              |
| -------- | --------------------- |
| 加入房间 | enterRoom:[roomId]    |
| 退出房间 | exitRoom:[roomId]     |
| 重置房间 | restartRoom:[roomId]  |
| 发送消息 | sendMessage:[message] |

- 服务端指令

| 指令名称 | 指令格式                             |
| -------- | ------------------------------------ |
| 发送消息 | enterRoom:[roomId]                   |
| 有人加入 | roomEntered:[id] [username] [avatar] |
| 有人退出 | roomExited:[roomId]                  |
