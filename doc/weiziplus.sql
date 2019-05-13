/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : weiziplus

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 12/05/2019 11:22:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_function`;
CREATE TABLE `sys_function`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统功能表主键，自增',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '上级id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能唯一标识',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能标题',
  `type` int(2) NOT NULL DEFAULT 0 COMMENT '功能类型;0:菜单,1:按钮',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'el-icon-info' COMMENT '功能图标',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '功能排序，数字越小越靠前',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '功能创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE COMMENT '功能名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_function
-- ----------------------------
INSERT INTO `sys_function` VALUES (1, 0, 'system', '系统管理', 0, 'el-icon-info', 0, '描述', '2019-05-09 16:55:47');
INSERT INTO `sys_function` VALUES (2, 1, 'sysFunction', '功能管理', 0, 'el-icon-info', 0, '', '2019-05-09 16:56:10');
INSERT INTO `sys_function` VALUES (3, 1, 'sysRole', '角色管理', 0, 'el-icon-info', 1, '', '2019-05-09 16:56:26');
INSERT INTO `sys_function` VALUES (4, 1, 'sysUser', '用户管理', 0, 'el-icon-info', 2, '', '2019-05-09 16:56:52');
INSERT INTO `sys_function` VALUES (5, 2, 'sysFunc_add', '新增', 1, 'el-icon-info', 0, '', '2019-05-10 10:20:41');
INSERT INTO `sys_function` VALUES (6, 2, 'sysFunc_update', '修改', 1, 'el-icon-info', 0, '', '2019-05-10 10:22:00');
INSERT INTO `sys_function` VALUES (7, 2, 'sysFunc_delete', '删除', 1, 'el-icon-info', 0, '', '2019-05-10 10:22:34');
INSERT INTO `sys_function` VALUES (8, 3, 'sysRole_add', '新增', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:08');
INSERT INTO `sys_function` VALUES (9, 3, 'sysRole_update', '修改', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:21');
INSERT INTO `sys_function` VALUES (10, 3, 'sysRole_delete', '删除', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:53');
INSERT INTO `sys_function` VALUES (11, 3, 'sysRole_save', '保存', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:07');
INSERT INTO `sys_function` VALUES (12, 4, 'sysUser_add', '新增', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:43');
INSERT INTO `sys_function` VALUES (13, 4, 'sysUser_update', '修改', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:55');
INSERT INTO `sys_function` VALUES (14, 4, 'sysUser_delete', '删除', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:08');
INSERT INTO `sys_function` VALUES (15, 4, 'sysUser_role', '角色', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:43');
INSERT INTO `sys_function` VALUES (16, 4, 'sysUser_resetPwd', '重置密码', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:52');
INSERT INTO `sys_function` VALUES (21, 3, 'sysRole_status', '修改角色状态(启用/禁用)', 1, '', 4, '', '2019-05-10 15:58:15');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统角色表主键，自增',
  `parent_id` int(11) NOT NULL DEFAULT 1 COMMENT '上级角色id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `is_stop` int(2) NOT NULL DEFAULT 0 COMMENT '是否启用;0:启用,1:禁用',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序，数字越小越靠前',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `create_time` datetime(0) NOT NULL COMMENT '角色创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 0, '超级管理员', 0, 0, '超级管理员', '2019-05-09 16:27:52');
INSERT INTO `sys_role` VALUES (666, 1, '管理员', 0, 0, '', '2019-05-10 14:27:02');
INSERT INTO `sys_role` VALUES (1000000, 666, '测试', 0, 0, '测试', '2019-05-10 15:09:30');
INSERT INTO `sys_role` VALUES (1000001, 1000000, '测试下级', 0, 0, '', '2019-05-13 10:06:55');
INSERT INTO `sys_role` VALUES (1000002, 666, '管理员下级', 1, 0, '', '2019-05-13 10:07:04');

-- ----------------------------
-- Table structure for sys_role_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_function`;
CREATE TABLE `sys_role_function`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色功能表主键，自增',
  `role_id` int(11) NOT NULL COMMENT '角色表id',
  `function_id` int(11) NOT NULL COMMENT '功能表id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `function_id`(`function_id`) USING BTREE,
  CONSTRAINT `sys_role_function_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_role_function_ibfk_2` FOREIGN KEY (`function_id`) REFERENCES `sys_function` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_function
-- ----------------------------
INSERT INTO `sys_role_function` VALUES (85, 1, 1);
INSERT INTO `sys_role_function` VALUES (86, 1, 2);
INSERT INTO `sys_role_function` VALUES (87, 1, 5);
INSERT INTO `sys_role_function` VALUES (88, 1, 6);
INSERT INTO `sys_role_function` VALUES (89, 1, 7);
INSERT INTO `sys_role_function` VALUES (90, 1, 3);
INSERT INTO `sys_role_function` VALUES (91, 1, 8);
INSERT INTO `sys_role_function` VALUES (92, 1, 9);
INSERT INTO `sys_role_function` VALUES (93, 1, 10);
INSERT INTO `sys_role_function` VALUES (94, 1, 11);
INSERT INTO `sys_role_function` VALUES (95, 1, 21);
INSERT INTO `sys_role_function` VALUES (96, 1, 4);
INSERT INTO `sys_role_function` VALUES (97, 1, 12);
INSERT INTO `sys_role_function` VALUES (98, 1, 13);
INSERT INTO `sys_role_function` VALUES (99, 1, 14);
INSERT INTO `sys_role_function` VALUES (100, 1, 15);
INSERT INTO `sys_role_function` VALUES (101, 1, 16);
INSERT INTO `sys_role_function` VALUES (102, 666, 14);
INSERT INTO `sys_role_function` VALUES (103, 666, 15);
INSERT INTO `sys_role_function` VALUES (104, 666, 16);
INSERT INTO `sys_role_function` VALUES (105, 666, 1);
INSERT INTO `sys_role_function` VALUES (106, 666, 4);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统用户表主键id，自增',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '登录用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '登录密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户真实姓名',
  `role_id` int(11) NOT NULL DEFAULT 0 COMMENT '系统角色表id',
  `allow_login` int(2) NOT NULL DEFAULT 0 COMMENT '是否允许登录;0:允许，1:禁止',
  `last_active_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户最后活跃时间',
  `create_time` datetime(0) NOT NULL COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE COMMENT '登录名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'ebe0b26e0c99fbf05e44de4e118f42d2', 'admin', 1, 0, '2019-05-13 10:11:29', '2019-05-09 16:26:15');
INSERT INTO `sys_user` VALUES (666, 'weiziplus', 'ebe0b26e0c99fbf05e44de4e118f42d2', 'weiziplus', 666, 0, '2019-05-10 15:59:31', '2019-05-10 14:30:04');
INSERT INTO `sys_user` VALUES (1000000, 'ss', 'b6eaced172f2ae2c94eb5c1de8c72921', '666', 0, 0, '2019-05-13 09:14:32', '2019-05-13 09:14:32');

-- ----------------------------
-- Table structure for w_user
-- ----------------------------
DROP TABLE IF EXISTS `w_user`;
CREATE TABLE `w_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户表主键，自增',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '密码',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of w_user
-- ----------------------------
INSERT INTO `w_user` VALUES (1, 'weiziplus', 'ebe0b26e0c99fbf05e44de4e118f42d2', '2019-05-10 17:25:32');
INSERT INTO `w_user` VALUES (1000001, 'ceshi', 'ebe0b26e0c99fbf05e44de4e118f42d2', '2019-05-10 17:54:22');

SET FOREIGN_KEY_CHECKS = 1;
