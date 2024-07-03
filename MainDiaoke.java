typescript上面使用egg.js框架制作restful API接口 CRUD mongoDB、Redis和Mysql
1.首先，确保您安装了 Node.js 和 npm。然后，创建一个新的 Egg.js 项目并安装所需的依赖：
npm init egg --type=ts
cd your_project_name
npm install egg-mongoose mongoose egg-redis @types/redis egg-mysql @types/mysql
2.在 config/plugin.ts 中配置数据库插件：
export default {
  mongoose: {
    enable: true,
    package: 'egg-mongoose',
  },
  redis: {
    enable: true,
    package: 'egg-redis',
  },
  mysql: {
    enable: true,
    package: 'egg-mysql',
  },
};
3.config/config.default.ts 中配置数据库连接信息：
export default {
  mongoose: {
    url: 'your_mongodb_connection_string',
  },
  redis: {
    client: {
      host: 'your_redis_host',
      port: your_redis_port,
      password: 'your_redis_password',
    },
  },
  mysql: {
    // MySQL 配置
    host: 'your_mysql_host',
    port: your_mysql_port,
    user: 'your_mysql_user',
    password: 'your_mysql_password',
    database: 'your_mysql_database',
  },
};
4.对于 MongoDB，创建一个 mongoose 模型
import { Schema, model } from'mongoose';

interface IUser {
  name: string;
  age: number;
}

const userSchema = new Schema<IUser>({
  name: { type: String, required: true },
  age: { type: Number, required: true },
});

export const User = model<IUser>('User', userSchema);
5. MySQL，创建一个数据库表映射的模型
import { BaseEntity, Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity('users')
export class User extends BaseEntity {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  name: string;

  @Column()
  age: number;
}
6.创建一个控制器来处理 RESTful API 的请求
import { Controller, Get, Post, Put, Delete, Param } from '@nestjs/common';
import { User } from './models/user.model';

@Controller('users')
export class UserController {

  @Get()
  async getAllUsers() {
    // 从 MongoDB 获取所有用户
    const users = await User.find();
    return users;
  }

  @Get(':id')
  async getUserById(@Param('id') id: string) {
    // 从 MongoDB 根据 ID 获取用户
    const user = await User.findById(id);
    return user;
  }

  @Post()
  async createUser() {
    // 创建新用户并保存到 MongoDB
    const newUser = new User({ name: 'John', age: 25 });
    await newUser.save();
    return newUser;
  }

  @Put(':id')
  async updateUser(@Param('id') id: string) {
    // 根据 ID 更新用户在 MongoDB 中的信息
    const updatedUser = await User.findByIdAndUpdate(id, { age: 30 }, { new: true });
    return updatedUser;
  }

  @Delete(':id')
  async deleteUser(@Param('id') id: string) {
    // 根据 ID 删除用户在 MongoDB 中的记录
    await User.findByIdAndDelete(id);
    return { message: 'User deleted successfully' };
  }
}
7.在 router.ts 中注册控制器的路由
import { Router } from '@nestjs/core';
import { UserController } from './controllers/user.controller';

const router = new Router();

router.use('/users', UserController);

export default router;