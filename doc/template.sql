/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : template

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 06/08/2019 08:57:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary`;
CREATE TABLE `data_dictionary`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '字典标识',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '字典名字',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '字典创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_dictionary
-- ----------------------------
INSERT INTO `data_dictionary` VALUES (1, 'ipFilter', 'ipFilter', 'ip名单', '2019-09-18 14:43:27');
INSERT INTO `data_dictionary` VALUES (2, 'abnormalIp', 'abnormalIp', '异常ip', '2019-09-18 15:55:16');

-- ----------------------------
-- Table structure for data_dictionary_value
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary_value`;
CREATE TABLE `data_dictionary_value`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `dictionary_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '字典编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '值',
  `type` tinyint(3) NOT NULL DEFAULT 0 COMMENT '类型(自定义)：\r\nipFilter：ip名单---0：白名单，1：黑名单',
  `order` int(11) NOT NULL DEFAULT 0 COMMENT '排序(自定义,默认为排序)\r\n abnormalIp:异常ip---异常出错次数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dictionary_code`(`dictionary_code`) USING BTREE,
  CONSTRAINT `data_dictionary_value_ibfk_1` FOREIGN KEY (`dictionary_code`) REFERENCES `data_dictionary` (`code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_dictionary_value
-- ----------------------------
INSERT INTO `data_dictionary_value` VALUES (1, 'ipFilter', '127.0.0.1', '127.0.0.1', 0, 0, '本地，ip白名单', '2019-09-18 14:44:08');
INSERT INTO `data_dictionary_value` VALUES (2, 'abnormalIp', '1.1.1.1', '1.1.1.1', 0, 2, '测试ip', '2019-09-18 15:56:03');

-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_function`;
CREATE TABLE `sys_function`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统功能表主键，自增',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '上级id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能唯一标识',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能路径',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能标题',
  `contain_api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '当前功能对应的api列表，多个用,隔开',
  `type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '功能类型;0:菜单,1:按钮',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'el-icon-info' COMMENT '功能图标',
  `sort` tinyint(2) NOT NULL DEFAULT 0 COMMENT '功能排序，数字越小越靠前',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '功能创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE COMMENT '功能名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_function
-- ----------------------------
INSERT INTO `sys_function` VALUES (1, 0, 'home', 'home', '首页', '', 0, 'el-icon-s-home', 0, '后台首页', '2019-08-26 14:13:35');
INSERT INTO `sys_function` VALUES (2, 0, 'system', 'system', '系统管理', '', 0, 'el-icon-s-tools', 1, '后台的管理', '2019-05-09 16:55:47');
INSERT INTO `sys_function` VALUES (3, 2, 'sysFunction', 'sysFunction', '功能管理', '/pc/sysFunction/getAllFunctionTreeNotButton,/pc/sysFunction/getFunctionList', 0, 'el-icon-s-tools', 0, '管理后台的菜单、按钮等。对应api可以将权限精确到接口', '2019-05-09 16:56:10');
INSERT INTO `sys_function` VALUES (4, 2, 'sysRole', 'sysRole', '角色管理', '/pc/sysRole/getRoleTree,/pc/sysFunction/getAllFunctionTree,/pc/sysFunction/getRoleFunList', 0, 'el-icon-s-check', 1, '管理后台系统的各项角色，以及角色所拥有的功能等', '2019-05-09 16:56:26');
INSERT INTO `sys_function` VALUES (5, 2, 'sysUser', 'sysUser', '用户管理', '/pc/sysUser/getPageList,/pc/sysRole/getRoleList', 0, 'el-icon-s-custom', 2, '管理后台的用户', '2019-05-09 16:56:52');
INSERT INTO `sys_function` VALUES (6, 2, 'sysLog', 'sysLog', '日志管理', '/pc/sysLog/getPageList,/pc/sysRole/getRoleList', 0, 'el-icon-s-order', 3, '记录系统用户的操作', '2019-05-13 15:30:57');
INSERT INTO `sys_function` VALUES (7, 2, 'dataDictionaryIpFilter', 'ipFilter', 'ip名单', '/pc/dataDictionary/ipFilter/getPageList', 0, 'el-icon-platform-eleme', 4, '对ip进行限制，白名单将不进行任何限制，黑名单将拒绝访问', '2019-08-05 10:13:42');
INSERT INTO `sys_function` VALUES (8, 2, 'dataDictionaryAbnormalIp', 'abnormalIp', '异常ip管理', '/pc/dataDictionary/abnormalIp/getPageList', 0, 'el-icon-s-help', 5, '展示异常访问的ip，如：请求过快等，以便于进行安全排查', '2019-08-05 14:39:33');
INSERT INTO `sys_function` VALUES (9, 3, 'sysFunc_add', 'add', '新增', '/pc/sysFunction/addFunction', 1, 'el-icon-info', 0, '', '2019-05-10 10:20:41');
INSERT INTO `sys_function` VALUES (10, 3, 'sysFunc_update', 'update', '修改', '/pc/sysFunction/updateFunction', 1, 'el-icon-info', 0, '', '2019-05-10 10:22:00');
INSERT INTO `sys_function` VALUES (11, 3, 'sysFunc_delete', 'delete', '删除', '/pc/sysFunction/deleteFunction', 1, 'el-icon-info', 0, '', '2019-05-10 10:22:34');
INSERT INTO `sys_function` VALUES (12, 4, 'sysRole_add', 'add', '新增', '/pc/sysRole/addRole', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:08');
INSERT INTO `sys_function` VALUES (13, 4, 'sysRole_update', 'update', '修改', '/pc/sysRole/updateRole', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:21');
INSERT INTO `sys_function` VALUES (14, 4, 'sysRole_delete', 'delete', '删除', '/pc/sysRole/deleteRole', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:53');
INSERT INTO `sys_function` VALUES (15, 4, 'sysRole_save', 'save', '保存', '/pc/sysRole/addRoleFun', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:07');
INSERT INTO `sys_function` VALUES (16, 4, 'sysRole_status', 'status', '修改角色状态(启用/禁用)', '/pc/sysRole/changeRoleIsStop', 1, '', 4, '', '2019-05-10 15:58:15');
INSERT INTO `sys_function` VALUES (17, 5, 'sysUser_add', 'add', '新增', '/pc/sysUser/addUser', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:43');
INSERT INTO `sys_function` VALUES (18, 5, 'sysUser_update', 'update', '修改', '/pc/sysUser/updateUser', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:55');
INSERT INTO `sys_function` VALUES (19, 5, 'sysUser_delete', 'delete', '删除', '/pc/sysUser/deleteUser', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:08');
INSERT INTO `sys_function` VALUES (20, 5, 'sysUser_role', 'role', '角色', '/pc/sysUser/updateUserRole', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:43');
INSERT INTO `sys_function` VALUES (21, 5, 'sysUser_resetPwd', 'resetPwd', '重置密码', '/pc/sysUser/resetUserPassword', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:52');
INSERT INTO `sys_function` VALUES (22, 0, 'tools', 'tools', '常用工具', '', 0, 'el-icon-s-promotion', 2, '进一步封装element-ui的常用组件，约定大于配置思想', '2019-08-24 16:41:28');
INSERT INTO `sys_function` VALUES (23, 22, 'toolsUpload', 'upload', '图片上传', '', 0, 'el-icon-picture', 0, '进一步封装了图片上传，方便调用', '2019-08-24 16:41:49');
INSERT INTO `sys_function` VALUES (24, 22, 'richText', 'richText', '富文本', '', 0, 'el-icon-s-release', 1, '进一步封装了富文本，方便调用', '2019-09-04 16:21:09');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统日志表主键，自增',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户表id',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作描述',
  `ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `sys_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, 1, '查看系统日志', '127.0.0.1', '2019-08-06 08:55:51');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '系统角色表主键，自增',
  `parent_id` bigint(11) NOT NULL DEFAULT 1 COMMENT '上级角色id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `is_stop` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否启用;0:启用,1:禁用',
  `sort` tinyint(2) NOT NULL DEFAULT 0 COMMENT '排序，数字越小越靠前',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '角色创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

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
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '角色功能表主键，自增',
  `role_id` bigint(11) NOT NULL COMMENT '角色表id',
  `function_id` bigint(11) NOT NULL COMMENT '功能表id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `function_id`(`function_id`) USING BTREE,
  CONSTRAINT `sys_role_function_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_role_function_ibfk_2` FOREIGN KEY (`function_id`) REFERENCES `sys_function` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 274 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_function
-- ----------------------------
INSERT INTO `sys_role_function` VALUES (231, 1, 1);
INSERT INTO `sys_role_function` VALUES (232, 1, 2);
INSERT INTO `sys_role_function` VALUES (233, 1, 3);
INSERT INTO `sys_role_function` VALUES (234, 1, 9);
INSERT INTO `sys_role_function` VALUES (235, 1, 10);
INSERT INTO `sys_role_function` VALUES (236, 1, 11);
INSERT INTO `sys_role_function` VALUES (237, 1, 7);
INSERT INTO `sys_role_function` VALUES (238, 1, 8);
INSERT INTO `sys_role_function` VALUES (239, 1, 4);
INSERT INTO `sys_role_function` VALUES (240, 1, 12);
INSERT INTO `sys_role_function` VALUES (241, 1, 13);
INSERT INTO `sys_role_function` VALUES (242, 1, 14);
INSERT INTO `sys_role_function` VALUES (243, 1, 15);
INSERT INTO `sys_role_function` VALUES (244, 1, 16);
INSERT INTO `sys_role_function` VALUES (245, 1, 5);
INSERT INTO `sys_role_function` VALUES (246, 1, 17);
INSERT INTO `sys_role_function` VALUES (247, 1, 18);
INSERT INTO `sys_role_function` VALUES (248, 1, 19);
INSERT INTO `sys_role_function` VALUES (249, 1, 20);
INSERT INTO `sys_role_function` VALUES (250, 1, 21);
INSERT INTO `sys_role_function` VALUES (251, 1, 6);
INSERT INTO `sys_role_function` VALUES (252, 1, 22);
INSERT INTO `sys_role_function` VALUES (253, 1, 23);
INSERT INTO `sys_role_function` VALUES (254, 1, 24);
INSERT INTO `sys_role_function` VALUES (255, 666, 1);
INSERT INTO `sys_role_function` VALUES (256, 666, 7);
INSERT INTO `sys_role_function` VALUES (257, 666, 8);
INSERT INTO `sys_role_function` VALUES (258, 666, 4);
INSERT INTO `sys_role_function` VALUES (259, 666, 12);
INSERT INTO `sys_role_function` VALUES (260, 666, 13);
INSERT INTO `sys_role_function` VALUES (261, 666, 14);
INSERT INTO `sys_role_function` VALUES (262, 666, 15);
INSERT INTO `sys_role_function` VALUES (263, 666, 16);
INSERT INTO `sys_role_function` VALUES (264, 666, 5);
INSERT INTO `sys_role_function` VALUES (265, 666, 17);
INSERT INTO `sys_role_function` VALUES (266, 666, 18);
INSERT INTO `sys_role_function` VALUES (267, 666, 19);
INSERT INTO `sys_role_function` VALUES (268, 666, 20);
INSERT INTO `sys_role_function` VALUES (269, 666, 21);
INSERT INTO `sys_role_function` VALUES (270, 666, 6);
INSERT INTO `sys_role_function` VALUES (271, 666, 22);
INSERT INTO `sys_role_function` VALUES (272, 666, 23);
INSERT INTO `sys_role_function` VALUES (273, 666, 24);
INSERT INTO `sys_role_function` VALUES (274, 666, 2);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统用户表主键id，自增',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '登录用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '登录密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户真实姓名',
  `role_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '系统角色表id',
  `allow_login` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否允许登录;0:允许，1:禁止，2:封号中',
  `suspend_num` int(5) NOT NULL DEFAULT 0 COMMENT '账户封号次数',
  `last_ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户最后活跃ip地址',
  `last_active_time` datetime(0) NULL DEFAULT NULL COMMENT '用户最后活跃时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE COMMENT '登录名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1000003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'superadmin', '31b08a07bafd1363869790e5f757afc1', 'superadmin', 1, 0, 0, '127.0.0.1', '2019-08-06 08:56:40', '2019-05-09 16:26:15');
INSERT INTO `sys_user` VALUES (666, 'weiziplus', 'ebe0b26e0c99fbf05e44de4e118f42d2', 'weiziplus', 666, 2, 7, '', '2019-08-01 17:16:24', '2019-05-10 14:30:04');
INSERT INTO `sys_user` VALUES (1000000, 'admin', '31b08a07bafd1363869790e5f757afc1', 'qqq', 666, 0, 0, '', '2019-05-13 09:14:32', '2019-08-07 09:38:16');
INSERT INTO `sys_user` VALUES (1000003, 'qq', 'ebe0b26e0c99fbf05e44de4e118f42d2', 'qqqqqqqqqqqqq', 666, 0, 0, '', '2019-06-28 17:21:07', '2019-06-28 17:18:24');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户表主键，自增',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '密码',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'weiziplus', 'ebe0b26e0c99fbf05e44de4e118f42d2', '2019-05-10 17:25:32');
INSERT INTO `user` VALUES (1000001, 'ceshi', 'ebe0b26e0c99fbf05e44de4e118f42d2', '2019-05-10 17:54:22');

SET FOREIGN_KEY_CHECKS = 1;
