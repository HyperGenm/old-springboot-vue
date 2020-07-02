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
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典标识',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '字典创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_dictionary
-- ----------------------------
INSERT INTO `data_dictionary` VALUES (1, 'ipRole', 'ip规则', 'ip规则，全部允许，只允许白名单，只限制黑名单', '2020-02-24 20:00:35');
INSERT INTO `data_dictionary` VALUES (2, 'ipListWhite', 'ip白名单', 'ip白名单', '2020-02-24 20:46:10');
INSERT INTO `data_dictionary` VALUES (3, 'ipListBlack', 'ip黑名单', 'ip黑名单', '2020-02-24 21:15:06');
INSERT INTO `data_dictionary` VALUES (4, 'ipListAbnormal', '异常ip', '异常ip', '2020-02-24 21:15:29');

-- ----------------------------
-- Table structure for data_dictionary_value
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary_value`;
CREATE TABLE `data_dictionary_value`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `dictionary_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典编号',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '值',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT '含义自定\r\n   1:ipListAbnormal---异常次数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dictionary_code`(`dictionary_code`) USING BTREE,
  CONSTRAINT `data_dictionary_value_ibfk_1` FOREIGN KEY (`dictionary_code`) REFERENCES `data_dictionary` (`code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_dictionary_value
-- ----------------------------
INSERT INTO `data_dictionary_value` VALUES (1, 'ipRole', 'all', 'ip规则', 0, 'ip限制的规则', '2020-02-24 20:20:44');
INSERT INTO `data_dictionary_value` VALUES (2, 'ipListWhite', '127.0.0.1', '本地', 0, '本地,ip白名单', '2020-02-24 20:47:02');
INSERT INTO `data_dictionary_value` VALUES (3, 'ipListBlack', '0.0.0.0', '测试', 0, '测试数据', '2020-02-24 21:15:52');

-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_function`;
CREATE TABLE `sys_function`  (
  `id` int(7) NOT NULL AUTO_INCREMENT COMMENT '系统功能表主键，自增',
  `parent_id` int(7) NOT NULL DEFAULT 0 COMMENT '上级id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能唯一标识',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能路径',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能标题',
  `contain_api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '当前功能对应的api列表，多个用,隔开',
  `type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '功能类型;0:菜单,1:按钮',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'el-icon-info' COMMENT '功能图标',
  `sort` tinyint(2) NOT NULL DEFAULT 0 COMMENT '功能排序，数字越小越靠前',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '功能创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE COMMENT '功能名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_function
-- ----------------------------
INSERT INTO `sys_function` VALUES (1, 0, 'system', 'system', '系统管理', '', 0, 'el-icon-s-tools', 1, '后台的管理', '2019-05-09 16:55:47');
INSERT INTO `sys_function` VALUES (2, 1, 'sysFunction', 'sysFunction', '功能管理', '/pc/sysFunction/getAllFunctionTreePageList,/pc/sysFunction/getAllFunctionTreeNotButton', 0, 'el-icon-s-tools', 7, '管理后台的菜单、按钮等。对应api可以将权限精确到接口', '2019-05-09 16:56:10');
INSERT INTO `sys_function` VALUES (3, 1, 'sysRole', 'sysRole', '角色管理', '/pc/sysRole/getAllRoleTreePageList,/pc/sysRole/getRoleList,/pc/sysFunction/getAllFunctionTree', 0, 'el-icon-s-check', 2, '管理后台系统的各项角色，以及角色所拥有的功能等', '2019-05-09 16:56:26');
INSERT INTO `sys_function` VALUES (4, 1, 'sysUser', 'sysUser', '用户管理', '/pc/sysUser/getPageList,/pc/sysRole/getRoleList', 0, 'el-icon-s-custom', 1, '管理后台的用户', '2019-05-09 16:56:52');
INSERT INTO `sys_function` VALUES (5, 1, 'sysUserLog', 'sysUserLog', '系统日志管理', '/pc/sysUserLog/getPageList,/pc/sysRole/getRoleList', 0, 'el-icon-s-order', 4, '记录系统用户的操作', '2019-05-13 15:30:57');
INSERT INTO `sys_function` VALUES (6, 2, 'sysFunc_update', 'update', '修改', '/pc/sysFunction/updateFunction', 1, 'el-icon-info', 0, '', '2019-05-10 10:22:00');
INSERT INTO `sys_function` VALUES (7, 3, 'sysRole_add', 'add', '新增', '/pc/sysRole/addRole', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:08');
INSERT INTO `sys_function` VALUES (8, 3, 'sysRole_update', 'update', '修改', '/pc/sysRole/updateRole', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:21');
INSERT INTO `sys_function` VALUES (9, 3, 'sysRole_delete', 'delete', '删除', '/pc/sysRole/deleteRole', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:53');
INSERT INTO `sys_function` VALUES (10, 3, 'sysRole_save', 'save', '修改功能', '/pc/sysRole/addRoleFun', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:07');
INSERT INTO `sys_function` VALUES (11, 3, 'sysRole_status', 'status', '修改角色状态(启用/禁用)', '/pc/sysRole/changeRoleIsStop', 1, '', 4, '', '2019-05-10 15:58:15');
INSERT INTO `sys_function` VALUES (12, 4, 'sysUser_add', 'add', '新增', '/pc/sysUser/addUser', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:43');
INSERT INTO `sys_function` VALUES (13, 4, 'sysUser_update', 'update', '修改', '/pc/sysUser/updateUser', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:55');
INSERT INTO `sys_function` VALUES (14, 4, 'sysUser_delete', 'delete', '删除', '/pc/sysUser/deleteUser', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:08');
INSERT INTO `sys_function` VALUES (15, 4, 'sysUser_role', 'role', '角色', '/pc/sysUser/updateUserRole', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:43');
INSERT INTO `sys_function` VALUES (16, 4, 'sysUser_resetPwd', 'resetPwd', '重置密码', '/pc/sysUser/resetUserPassword', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:52');
INSERT INTO `sys_function` VALUES (17, 0, 'tools', 'tools', '常用工具', '', 0, 'el-icon-s-promotion', 2, '进一步封装element-ui的常用组件，约定大于配置思想', '2019-08-24 16:41:28');
INSERT INTO `sys_function` VALUES (18, 17, 'toolsUpload', 'upload', '图片上传', '', 0, 'el-icon-picture', 0, '进一步封装了图片上传，方便调用', '2019-08-24 16:41:49');
INSERT INTO `sys_function` VALUES (19, 17, 'richText', 'richText', '富文本', '', 0, 'el-icon-s-release', 1, '进一步封装了富文本，方便调用', '2019-09-04 16:21:09');
INSERT INTO `sys_function` VALUES (20, 1, 'ipManager', 'ipManager', 'ip管理', '', 0, '', 3, '对ip进行管理', '2020-02-24 20:04:42');
INSERT INTO `sys_function` VALUES (21, 20, 'ipManager_get', 'ipManager_get', '查看', '/pc/dataDictionary/ipManager/getIpRole,/pc/dataDictionary/ipManager/getIpList', 1, '', 0, '', '2020-02-25 15:50:24');
INSERT INTO `sys_function` VALUES (22, 1, 'userLog', 'userLog', '用户日志', '/pc/userLog/getPageList', 0, 'el-icon-s-order', 5, '用户日志', '2020-02-28 20:44:20');
INSERT INTO `sys_function` VALUES (23, 1, 'sysError', 'sysError', '系统异常', '/pc/sysError/getPageList', 0, 'el-icon-s-order', 7, '系统异常', '2020-02-28 20:44:20');

-- ----------------------------
-- Table structure for sys_user_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_log`;
CREATE TABLE `sys_user_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统日志表主键，自增',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户表id',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求的路径',
  `param` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '当前请求的参数',
  `type` int(2) NOT NULL DEFAULT '1' COMMENT '请求的类型,1:查询,2:新增,3:修改,4:删除',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作描述',
  `ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `sys_user_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_log
-- ----------------------------
INSERT INTO `sys_user_log` VALUES (1, 1, '/pc/sysUserLog/getPageList', '{\"pageSize\":[\"20\"],\"pageNum\":[\"1\"]}', 1, '查看系统用户日志', '127.0.0.1',  '2019-08-06 08:55:51');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(7) NOT NULL AUTO_INCREMENT COMMENT '系统角色表主键，自增',
  `parent_id` int(7) NOT NULL DEFAULT 1 COMMENT '上级角色id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `is_stop` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否启用;0:启用,1:禁用',
  `sort` tinyint(2) NOT NULL DEFAULT 0 COMMENT '排序，数字越小越靠前',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '角色创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

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
  `id` bigint(17) NOT NULL AUTO_INCREMENT COMMENT '角色功能表主键，自增',
  `role_id` int(7) NOT NULL COMMENT '角色表id',
  `function_id` int(7) NOT NULL COMMENT '功能表id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `function_id`(`function_id`) USING BTREE,
  CONSTRAINT `sys_role_function_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_role_function_ibfk_2` FOREIGN KEY (`function_id`) REFERENCES `sys_function` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 385 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_function
-- ----------------------------
INSERT INTO `sys_role_function` VALUES (341, 1, 1);
INSERT INTO `sys_role_function` VALUES (342, 1, 4);
INSERT INTO `sys_role_function` VALUES (343, 1, 12);
INSERT INTO `sys_role_function` VALUES (344, 1, 13);
INSERT INTO `sys_role_function` VALUES (345, 1, 14);
INSERT INTO `sys_role_function` VALUES (346, 1, 15);
INSERT INTO `sys_role_function` VALUES (347, 1, 16);
INSERT INTO `sys_role_function` VALUES (348, 1, 3);
INSERT INTO `sys_role_function` VALUES (349, 1, 7);
INSERT INTO `sys_role_function` VALUES (350, 1, 8);
INSERT INTO `sys_role_function` VALUES (351, 1, 9);
INSERT INTO `sys_role_function` VALUES (352, 1, 10);
INSERT INTO `sys_role_function` VALUES (353, 1, 11);
INSERT INTO `sys_role_function` VALUES (354, 1, 20);
INSERT INTO `sys_role_function` VALUES (355, 1, 21);
INSERT INTO `sys_role_function` VALUES (356, 1, 5);
INSERT INTO `sys_role_function` VALUES (357, 1, 2);
INSERT INTO `sys_role_function` VALUES (358, 1, 6);
INSERT INTO `sys_role_function` VALUES (359, 1, 17);
INSERT INTO `sys_role_function` VALUES (360, 1, 18);
INSERT INTO `sys_role_function` VALUES (361, 1, 19);
INSERT INTO `sys_role_function` VALUES (362, 666, 1);
INSERT INTO `sys_role_function` VALUES (363, 666, 4);
INSERT INTO `sys_role_function` VALUES (364, 666, 12);
INSERT INTO `sys_role_function` VALUES (365, 666, 13);
INSERT INTO `sys_role_function` VALUES (366, 666, 14);
INSERT INTO `sys_role_function` VALUES (367, 666, 15);
INSERT INTO `sys_role_function` VALUES (368, 666, 16);
INSERT INTO `sys_role_function` VALUES (369, 666, 3);
INSERT INTO `sys_role_function` VALUES (370, 666, 7);
INSERT INTO `sys_role_function` VALUES (371, 666, 8);
INSERT INTO `sys_role_function` VALUES (372, 666, 9);
INSERT INTO `sys_role_function` VALUES (373, 666, 10);
INSERT INTO `sys_role_function` VALUES (374, 666, 11);
INSERT INTO `sys_role_function` VALUES (375, 666, 20);
INSERT INTO `sys_role_function` VALUES (376, 666, 21);
INSERT INTO `sys_role_function` VALUES (377, 666, 5);
INSERT INTO `sys_role_function` VALUES (378, 666, 2);
INSERT INTO `sys_role_function` VALUES (379, 666, 6);
INSERT INTO `sys_role_function` VALUES (380, 666, 17);
INSERT INTO `sys_role_function` VALUES (381, 666, 18);
INSERT INTO `sys_role_function` VALUES (382, 666, 19);
INSERT INTO `sys_role_function` VALUES (383, 1, 22);
INSERT INTO `sys_role_function` VALUES (385, 1, 23);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统用户表主键id，自增',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户真实姓名',
  `role_id` int(7) NOT NULL DEFAULT 0 COMMENT '系统角色表id',
  `allow_login` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否允许登录;0:允许，1:禁止',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '/logo.jpg' COMMENT '用户头像',
  `last_ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户最后活跃ip地址',
  `last_active_time` datetime(0) NULL DEFAULT NULL COMMENT '用户最后活跃时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE COMMENT '登录名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1000003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'superadmin', 'c8441c790cb26e52ce5a5e99a6c1945d', 'superadmin', 1, 0, '18888888888', '/logo.jpg', '127.0.0.1', '2020-02-25 20:35:19', '2019-05-09 16:26:15');
INSERT INTO `sys_user` VALUES (666, 'weiziplus', 'c8441c790cb26e52ce5a5e99a6c1945d', 'weiziplus', 666, 1, '18888888888', '/logo.jpg', '', '2019-08-01 17:16:24', '2019-05-10 14:30:04');
INSERT INTO `sys_user` VALUES (1000000, 'admin', 'c8441c790cb26e52ce5a5e99a6c1945d', 'qqq', 666, 0, '', '/logo.jpg', '', '2019-05-13 09:14:32', '2019-08-07 09:38:16');
INSERT INTO `sys_user` VALUES (1000001, 'qq', 'c8441c790cb26e52ce5a5e99a6c1945d', 'qqqqqqqqqqqqq', 666, 0, '18811111111', '/logo.jpg', '127.0.0.1', '2020-02-25 20:40:16', '2019-06-28 17:18:24');
INSERT INTO `sys_user` VALUES (1000002, 'qq1', 'c8441c790cb26e52ce5a5e99a6c1945d', 'qq', 0, 0, '', '/logo.jpg', '', NULL, '2020-02-21 15:14:30');
INSERT INTO `sys_user` VALUES (1000003, '用户名', 'c8441c790cb26e52ce5a5e99a6c1945d', '真实姓名', 0, 1, '', '/pc/user/icon/default.png', '', NULL, '2020-02-25 20:19:42');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户表主键，自增',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `last_ip_address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户最后活跃ip地址',
  `last_active_time` datetime(0) NULL DEFAULT NULL COMMENT '用户最后活跃时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'weiziplus', 'B7966B0719AF24AD7CBE954DDFB32DC0', 1, '127.0.0.1', '2020-06-01 09:42:12', '2020-06-01 09:34:31');

-- ----------------------------
-- Table structure for user_log
-- ----------------------------
DROP TABLE IF EXISTS `t_user_log`;
CREATE TABLE `t_user_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户表主键',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求的路径',
  `param` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '当前请求的参数',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '请求的类型,1:查询,2:新增,3:修改,4:删除',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作描述',
  `ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `user_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_log
-- ----------------------------
INSERT INTO `t_user_log` VALUES (1, 1, '/user/getInfo', '{\"pageSize\":[\"20\"],\"pageNum\":[\"1\"]}', 1,'获取用户信息', '127.0.0.1', '2020-02-28 21:02:46');

-- ----------------------------
-- Table structure for sys_error
-- ----------------------------
DROP TABLE IF EXISTS `sys_error`;
CREATE TABLE `sys_error`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类名',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '方法名',
  `line_number` int(11) NOT NULL DEFAULT 0 COMMENT '第几行',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `content` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '详情',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统异常' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_error
-- ----------------------------
INSERT INTO `sys_error` VALUES (1, 'org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator', 'doTranslate', 242,'系统捕获运行时异常', 'org.springframework.dao.DuplicateKeyException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: ', '2020-03-21 15:21:12');

SET FOREIGN_KEY_CHECKS = 1;
