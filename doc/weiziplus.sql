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
INSERT INTO `sys_function` VALUES (22, 1, 'sysLog', '日志管理', 0, '', 3, '系统日志', '2019-05-13 15:30:57');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统日志表主键，自增',
  `user_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户表id',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `sys_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, 1, '测试6666666666', '2019-05-13 15:24:00');
INSERT INTO `sys_log` VALUES (2, 1, '测试6666666666', '2019-05-13 15:24:02');
INSERT INTO `sys_log` VALUES (3, 1, '查看用户列表', '2019-05-13 15:30:29');
INSERT INTO `sys_log` VALUES (4, 1, '新增功能', '2019-05-13 15:30:57');
INSERT INTO `sys_log` VALUES (5, 1, '新增角色功能', '2019-05-13 15:31:04');
INSERT INTO `sys_log` VALUES (6, 1, '系统用户退出登录', '2019-05-13 15:31:07');
INSERT INTO `sys_log` VALUES (7, 1, '查看系统日志', '2019-05-13 15:46:29');
INSERT INTO `sys_log` VALUES (8, 1, '查看系统日志', '2019-05-13 15:47:18');
INSERT INTO `sys_log` VALUES (9, 1, '查看系统日志', '2019-05-13 15:47:46');
INSERT INTO `sys_log` VALUES (10, 1, '查看系统日志', '2019-05-13 15:48:10');
INSERT INTO `sys_log` VALUES (11, 1, '查看系统日志', '2019-05-13 15:48:19');
INSERT INTO `sys_log` VALUES (12, 1, '查看系统日志', '2019-05-13 15:48:45');
INSERT INTO `sys_log` VALUES (13, 1, '查看系统日志', '2019-05-13 15:48:45');
INSERT INTO `sys_log` VALUES (14, 1, '查看系统日志', '2019-05-13 15:48:46');
INSERT INTO `sys_log` VALUES (15, 1, '查看系统日志', '2019-05-13 15:49:27');
INSERT INTO `sys_log` VALUES (16, 1, '查看用户列表', '2019-05-13 15:49:42');
INSERT INTO `sys_log` VALUES (17, 1, '查看系统日志', '2019-05-13 15:49:43');
INSERT INTO `sys_log` VALUES (18, 1, '查看系统日志', '2019-05-13 15:49:45');
INSERT INTO `sys_log` VALUES (19, 1, '查看系统日志', '2019-05-13 15:49:48');
INSERT INTO `sys_log` VALUES (20, 1, '查看系统日志', '2019-05-13 15:49:49');
INSERT INTO `sys_log` VALUES (21, 1, '查看用户列表', '2019-05-13 15:49:58');
INSERT INTO `sys_log` VALUES (22, 1, '查看系统日志', '2019-05-13 15:49:59');
INSERT INTO `sys_log` VALUES (23, 1, '查看用户列表', '2019-05-13 15:50:06');
INSERT INTO `sys_log` VALUES (24, 1, '查看系统日志', '2019-05-13 15:51:35');
INSERT INTO `sys_log` VALUES (25, 1, '查看系统日志', '2019-05-13 15:51:44');
INSERT INTO `sys_log` VALUES (26, 1, '查看系统日志', '2019-05-13 15:51:46');
INSERT INTO `sys_log` VALUES (27, 1, '查看系统日志', '2019-05-13 15:51:46');
INSERT INTO `sys_log` VALUES (28, 1, '查看系统日志', '2019-05-13 15:53:23');
INSERT INTO `sys_log` VALUES (29, 1, '查看系统日志', '2019-05-13 15:53:52');
INSERT INTO `sys_log` VALUES (30, 1, '查看系统日志', '2019-05-13 15:57:56');
INSERT INTO `sys_log` VALUES (31, 1, '查看角色列表', '2019-05-13 15:57:56');
INSERT INTO `sys_log` VALUES (32, 1, '查看角色列表', '2019-05-13 15:57:59');
INSERT INTO `sys_log` VALUES (33, 1, '查看系统日志', '2019-05-13 15:57:59');
INSERT INTO `sys_log` VALUES (34, 1, '查看角色列表', '2019-05-13 15:58:51');
INSERT INTO `sys_log` VALUES (35, 1, '查看系统日志', '2019-05-13 15:58:51');
INSERT INTO `sys_log` VALUES (36, 1, '查看角色列表', '2019-05-13 15:59:33');
INSERT INTO `sys_log` VALUES (37, 1, '查看系统日志', '2019-05-13 15:59:33');
INSERT INTO `sys_log` VALUES (38, 1, '查看角色列表', '2019-05-13 15:59:45');
INSERT INTO `sys_log` VALUES (39, 1, '查看系统日志', '2019-05-13 15:59:45');
INSERT INTO `sys_log` VALUES (40, 1, '查看角色列表', '2019-05-13 16:00:31');
INSERT INTO `sys_log` VALUES (41, 1, '查看系统日志', '2019-05-13 16:00:31');
INSERT INTO `sys_log` VALUES (42, 1, '查看角色列表', '2019-05-13 16:00:46');
INSERT INTO `sys_log` VALUES (43, 1, '查看系统日志', '2019-05-13 16:00:46');
INSERT INTO `sys_log` VALUES (44, 1, '查看系统日志', '2019-05-13 16:44:13');
INSERT INTO `sys_log` VALUES (45, 1, '查看角色列表', '2019-05-13 16:44:13');
INSERT INTO `sys_log` VALUES (46, 1, '查看角色列表', '2019-05-13 16:44:28');
INSERT INTO `sys_log` VALUES (47, 1, '查看系统日志', '2019-05-13 16:44:28');
INSERT INTO `sys_log` VALUES (48, 1, '查看角色列表', '2019-05-13 16:44:43');
INSERT INTO `sys_log` VALUES (49, 1, '查看角色列表', '2019-05-13 16:44:48');
INSERT INTO `sys_log` VALUES (50, 1, '查看角色列表', '2019-05-13 16:45:28');
INSERT INTO `sys_log` VALUES (51, 1, '查看系统日志', '2019-05-13 16:45:28');
INSERT INTO `sys_log` VALUES (52, 1, '查看角色列表', '2019-05-13 16:46:58');
INSERT INTO `sys_log` VALUES (53, 1, '查看系统日志', '2019-05-13 16:46:58');
INSERT INTO `sys_log` VALUES (54, 1, '查看角色列表', '2019-05-13 16:47:05');
INSERT INTO `sys_log` VALUES (55, 1, '查看系统日志', '2019-05-13 16:47:05');
INSERT INTO `sys_log` VALUES (56, 1, '查看角色列表', '2019-05-13 16:47:46');
INSERT INTO `sys_log` VALUES (57, 1, '查看系统日志', '2019-05-13 16:47:46');
INSERT INTO `sys_log` VALUES (58, 1, '查看角色列表', '2019-05-13 16:50:45');
INSERT INTO `sys_log` VALUES (59, 1, '查看系统日志', '2019-05-13 16:50:45');
INSERT INTO `sys_log` VALUES (60, 1, '查看系统日志', '2019-05-13 16:57:45');
INSERT INTO `sys_log` VALUES (61, 1, '查看角色列表', '2019-05-13 16:57:45');
INSERT INTO `sys_log` VALUES (62, 1, '查看系统日志', '2019-05-13 16:57:56');
INSERT INTO `sys_log` VALUES (63, 1, '查看系统日志', '2019-05-13 17:02:12');
INSERT INTO `sys_log` VALUES (64, 1, '查看系统日志', '2019-05-13 17:03:24');
INSERT INTO `sys_log` VALUES (65, 1, '查看系统日志', '2019-05-13 17:03:29');
INSERT INTO `sys_log` VALUES (66, 1, '查看系统日志', '2019-05-13 17:03:32');
INSERT INTO `sys_log` VALUES (67, 1, '查看系统日志', '2019-05-13 17:03:34');
INSERT INTO `sys_log` VALUES (68, 1, '查看系统日志', '2019-05-13 17:03:37');
INSERT INTO `sys_log` VALUES (69, 1, '查看系统日志', '2019-05-13 17:03:39');
INSERT INTO `sys_log` VALUES (70, 1, '查看系统日志', '2019-05-13 17:03:43');
INSERT INTO `sys_log` VALUES (71, 1, '查看系统日志', '2019-05-13 17:03:46');
INSERT INTO `sys_log` VALUES (72, 1, '查看系统日志', '2019-05-13 17:03:51');
INSERT INTO `sys_log` VALUES (73, 1, '查看系统日志', '2019-05-13 17:04:16');
INSERT INTO `sys_log` VALUES (74, 1, '查看系统日志', '2019-05-13 17:04:25');
INSERT INTO `sys_log` VALUES (75, 1, '查看系统日志', '2019-05-13 17:04:28');
INSERT INTO `sys_log` VALUES (76, 1, '查看用户列表', '2019-05-13 17:04:30');
INSERT INTO `sys_log` VALUES (77, 1, '查看系统日志', '2019-05-13 17:04:32');
INSERT INTO `sys_log` VALUES (78, 1, '查看角色列表', '2019-05-13 17:04:32');
INSERT INTO `sys_log` VALUES (79, 1, '查看角色列表', '2019-05-13 17:05:20');
INSERT INTO `sys_log` VALUES (80, 1, '查看系统日志', '2019-05-13 17:05:20');
INSERT INTO `sys_log` VALUES (81, 1, '查看系统日志', '2019-05-13 17:05:28');
INSERT INTO `sys_log` VALUES (82, 1, '查看系统日志', '2019-05-13 17:05:30');
INSERT INTO `sys_log` VALUES (83, 1, '查看系统日志', '2019-05-13 17:07:10');
INSERT INTO `sys_log` VALUES (84, 1, '查看系统日志', '2019-05-13 17:07:17');
INSERT INTO `sys_log` VALUES (85, 1, '查看系统日志', '2019-05-13 17:07:19');
INSERT INTO `sys_log` VALUES (86, 1, '查看系统日志', '2019-05-13 17:07:21');
INSERT INTO `sys_log` VALUES (87, 1, '查看系统日志', '2019-05-13 17:07:22');
INSERT INTO `sys_log` VALUES (88, 1, '查看角色列表', '2019-05-13 17:08:55');
INSERT INTO `sys_log` VALUES (89, 1, '查看系统日志', '2019-05-13 17:08:55');
INSERT INTO `sys_log` VALUES (90, 1, '系统用户退出登录', '2019-05-13 17:09:04');
INSERT INTO `sys_log` VALUES (91, 666, '查看用户列表', '2019-05-13 17:09:17');
INSERT INTO `sys_log` VALUES (92, 666, '系统用户退出登录', '2019-05-13 17:09:20');
INSERT INTO `sys_log` VALUES (93, 1, '查看角色列表', '2019-05-13 17:09:31');
INSERT INTO `sys_log` VALUES (94, 1, '查看系统日志', '2019-05-13 17:09:31');
INSERT INTO `sys_log` VALUES (95, 1, '查看用户列表', '2019-05-13 17:10:20');

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
INSERT INTO `sys_role_function` VALUES (102, 666, 14);
INSERT INTO `sys_role_function` VALUES (103, 666, 15);
INSERT INTO `sys_role_function` VALUES (104, 666, 16);
INSERT INTO `sys_role_function` VALUES (105, 666, 1);
INSERT INTO `sys_role_function` VALUES (106, 666, 4);
INSERT INTO `sys_role_function` VALUES (107, 1, 1);
INSERT INTO `sys_role_function` VALUES (108, 1, 2);
INSERT INTO `sys_role_function` VALUES (109, 1, 5);
INSERT INTO `sys_role_function` VALUES (110, 1, 6);
INSERT INTO `sys_role_function` VALUES (111, 1, 7);
INSERT INTO `sys_role_function` VALUES (112, 1, 3);
INSERT INTO `sys_role_function` VALUES (113, 1, 8);
INSERT INTO `sys_role_function` VALUES (114, 1, 9);
INSERT INTO `sys_role_function` VALUES (115, 1, 10);
INSERT INTO `sys_role_function` VALUES (116, 1, 11);
INSERT INTO `sys_role_function` VALUES (117, 1, 21);
INSERT INTO `sys_role_function` VALUES (118, 1, 4);
INSERT INTO `sys_role_function` VALUES (119, 1, 12);
INSERT INTO `sys_role_function` VALUES (120, 1, 13);
INSERT INTO `sys_role_function` VALUES (121, 1, 14);
INSERT INTO `sys_role_function` VALUES (122, 1, 15);
INSERT INTO `sys_role_function` VALUES (123, 1, 16);
INSERT INTO `sys_role_function` VALUES (124, 1, 22);

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
INSERT INTO `sys_user` VALUES (1, 'admin', 'ebe0b26e0c99fbf05e44de4e118f42d2', 'admin', 1, 0, '2019-05-13 17:10:20', '2019-05-09 16:26:15');
INSERT INTO `sys_user` VALUES (666, 'weiziplus', 'ebe0b26e0c99fbf05e44de4e118f42d2', 'weiziplus', 666, 0, '2019-05-13 17:09:20', '2019-05-10 14:30:04');
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
