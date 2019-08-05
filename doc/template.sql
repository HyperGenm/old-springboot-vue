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

 Date: 05/08/2019 15:13:36
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_dictionary
-- ----------------------------
INSERT INTO `data_dictionary` VALUES (1, 'ipFilterWhite', 'ipFilterWhite', 'ip白名单', '2019-08-05 09:44:41');
INSERT INTO `data_dictionary` VALUES (2, 'ipFilterBlack', 'ipFilterBlack', 'ip黑名单', '2019-08-05 09:45:00');

-- ----------------------------
-- Table structure for data_dictionary_value
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary_value`;
CREATE TABLE `data_dictionary_value`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `dictionary_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '字典编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注',
  `order` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dictionary_code`(`dictionary_code`) USING BTREE,
  CONSTRAINT `data_dictionary_value_ibfk_1` FOREIGN KEY (`dictionary_code`) REFERENCES `data_dictionary` (`code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_dictionary_value
-- ----------------------------
INSERT INTO `data_dictionary_value` VALUES (7, 'ipFilterwhite', '127.0.0.1', '127.0.0.1', '', 0, '2019-08-05 14:15:14');

-- ----------------------------
-- Table structure for sys_abnormal_ip
-- ----------------------------
DROP TABLE IF EXISTS `sys_abnormal_ip`;
CREATE TABLE `sys_abnormal_ip`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `number` int(10) NOT NULL DEFAULT 0 COMMENT '次数',
  `lase_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近违规时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ip`(`ip`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '异常ip记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_abnormal_ip
-- ----------------------------
INSERT INTO `sys_abnormal_ip` VALUES (1, '1.1.1.1', 0, '2019-08-05 14:55:59', '2019-08-05 14:55:59');

-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_function`;
CREATE TABLE `sys_function`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '系统功能表主键，自增',
  `parent_id` bigint(11) NOT NULL DEFAULT 0 COMMENT '上级id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能唯一标识',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能路径',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能标题',
  `type` int(2) NOT NULL DEFAULT 0 COMMENT '功能类型;0:菜单,1:按钮',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'el-icon-info' COMMENT '功能图标',
  `sort` int(2) NOT NULL DEFAULT 0 COMMENT '功能排序，数字越小越靠前',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '功能描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '功能创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE COMMENT '功能名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_function
-- ----------------------------
INSERT INTO `sys_function` VALUES (1, 0, 'system', 'system', '系统管理', 0, 'el-icon-info', 0, '描述', '2019-05-09 16:55:47');
INSERT INTO `sys_function` VALUES (2, 1, 'sysFunction', 'sysFunction', '功能管理', 0, 'el-icon-info', 0, '', '2019-05-09 16:56:10');
INSERT INTO `sys_function` VALUES (3, 1, 'sysRole', 'sysRole', '角色管理', 0, 'el-icon-info', 1, '', '2019-05-09 16:56:26');
INSERT INTO `sys_function` VALUES (4, 1, 'sysUser', 'sysUser', '用户管理', 0, 'el-icon-info', 2, '', '2019-05-09 16:56:52');
INSERT INTO `sys_function` VALUES (5, 2, 'sysFunc_add', 'add', '新增', 1, 'el-icon-info', 0, '', '2019-05-10 10:20:41');
INSERT INTO `sys_function` VALUES (6, 2, 'sysFunc_update', 'update', '修改', 1, 'el-icon-info', 0, '', '2019-05-10 10:22:00');
INSERT INTO `sys_function` VALUES (7, 2, 'sysFunc_delete', 'delete', '删除', 1, 'el-icon-info', 0, '', '2019-05-10 10:22:34');
INSERT INTO `sys_function` VALUES (8, 3, 'sysRole_add', 'add', '新增', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:08');
INSERT INTO `sys_function` VALUES (9, 3, 'sysRole_update', 'update', '修改', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:21');
INSERT INTO `sys_function` VALUES (10, 3, 'sysRole_delete', 'delete', '删除', 1, 'el-icon-info', 0, '', '2019-05-10 10:23:53');
INSERT INTO `sys_function` VALUES (11, 3, 'sysRole_save', 'save', '保存', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:07');
INSERT INTO `sys_function` VALUES (12, 4, 'sysUser_add', 'add', '新增', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:43');
INSERT INTO `sys_function` VALUES (13, 4, 'sysUser_update', 'update', '修改', 1, 'el-icon-info', 0, '', '2019-05-10 10:24:55');
INSERT INTO `sys_function` VALUES (14, 4, 'sysUser_delete', 'delete', '删除', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:08');
INSERT INTO `sys_function` VALUES (15, 4, 'sysUser_role', 'role', '角色', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:43');
INSERT INTO `sys_function` VALUES (16, 4, 'sysUser_resetPwd', 'resetPwd', '重置密码', 1, 'el-icon-info', 0, '', '2019-05-10 10:25:52');
INSERT INTO `sys_function` VALUES (21, 3, 'sysRole_status', 'status', '修改角色状态(启用/禁用)', 1, '', 4, '', '2019-05-10 15:58:15');
INSERT INTO `sys_function` VALUES (22, 1, 'sysLog', 'sysLog', '日志管理', 0, '', 3, '系统日志', '2019-05-13 15:30:57');
INSERT INTO `sys_function` VALUES (24, 0, 'dataDictionary', 'dataDictionary', '数据字典', 0, '', 0, '数据字典', '2019-08-05 10:12:54');
INSERT INTO `sys_function` VALUES (25, 24, 'dataDictionaryIpFilter', 'ipFilter', 'ip名单', 0, '', 0, '', '2019-08-05 10:13:42');
INSERT INTO `sys_function` VALUES (27, 1, 'sysAbnormalIp', 'sysAbnormalIp', '异常ip管理', 0, '', 0, '', '2019-08-05 14:39:33');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '系统日志表主键，自增',
  `user_id` bigint(11) NOT NULL DEFAULT 0 COMMENT '用户表id',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `sys_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 751 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

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
INSERT INTO `sys_log` VALUES (96, 1, '查看功能列表', '2019-05-24 16:17:13');
INSERT INTO `sys_log` VALUES (97, 1, '查看角色树', '2019-05-24 16:17:14');
INSERT INTO `sys_log` VALUES (98, 1, '查看功能列表', '2019-05-24 16:17:14');
INSERT INTO `sys_log` VALUES (99, 1, '查看用户列表', '2019-05-24 16:17:15');
INSERT INTO `sys_log` VALUES (100, 1, '系统用户退出登录', '2019-05-24 16:17:16');
INSERT INTO `sys_log` VALUES (101, 1, '查看角色树', '2019-06-19 09:02:23');
INSERT INTO `sys_log` VALUES (102, 1, '查看用户列表', '2019-06-19 09:02:25');
INSERT INTO `sys_log` VALUES (103, 1, '查看角色列表', '2019-06-19 09:02:26');
INSERT INTO `sys_log` VALUES (104, 1, '查看角色树', '2019-06-19 09:02:27');
INSERT INTO `sys_log` VALUES (105, 1, '查看角色树', '2019-06-19 09:02:29');
INSERT INTO `sys_log` VALUES (106, 1, '查看角色树', '2019-06-19 09:04:45');
INSERT INTO `sys_log` VALUES (107, 1, '查看角色树', '2019-06-19 09:04:59');
INSERT INTO `sys_log` VALUES (108, 1, '查看角色树', '2019-06-19 09:05:00');
INSERT INTO `sys_log` VALUES (109, 1, '查看角色树', '2019-06-19 09:05:01');
INSERT INTO `sys_log` VALUES (110, 1, '查看角色树', '2019-06-19 09:05:45');
INSERT INTO `sys_log` VALUES (111, 1, '查看角色树', '2019-06-19 09:06:32');
INSERT INTO `sys_log` VALUES (112, 1, '查看角色树', '2019-06-19 09:07:30');
INSERT INTO `sys_log` VALUES (113, 1, '查看角色列表', '2019-06-19 09:46:04');
INSERT INTO `sys_log` VALUES (114, 1, '查看用户列表', '2019-06-19 09:46:05');
INSERT INTO `sys_log` VALUES (115, 1, '查看用户列表', '2019-06-19 09:46:10');
INSERT INTO `sys_log` VALUES (116, 1, '查看用户列表', '2019-06-19 09:47:48');
INSERT INTO `sys_log` VALUES (117, 1, '查看功能列表', '2019-06-19 09:48:12');
INSERT INTO `sys_log` VALUES (118, 1, '新增功能', '2019-06-19 09:48:20');
INSERT INTO `sys_log` VALUES (119, 1, '新增功能', '2019-06-19 09:48:24');
INSERT INTO `sys_log` VALUES (120, 1, '查看功能列表', '2019-06-19 09:48:25');
INSERT INTO `sys_log` VALUES (121, 1, '修改功能', '2019-06-19 09:48:34');
INSERT INTO `sys_log` VALUES (122, 1, '查看功能列表', '2019-06-19 09:48:34');
INSERT INTO `sys_log` VALUES (123, 1, '修改功能', '2019-06-19 09:48:41');
INSERT INTO `sys_log` VALUES (124, 1, '查看功能列表', '2019-06-19 09:48:41');
INSERT INTO `sys_log` VALUES (125, 1, '删除功能', '2019-06-19 09:48:43');
INSERT INTO `sys_log` VALUES (126, 1, '查看功能列表', '2019-06-19 09:48:44');
INSERT INTO `sys_log` VALUES (127, 1, '查看角色树', '2019-06-19 09:49:39');
INSERT INTO `sys_log` VALUES (128, 1, '查看功能列表', '2019-06-19 09:49:39');
INSERT INTO `sys_log` VALUES (129, 1, '改变角色状态', '2019-06-19 09:49:48');
INSERT INTO `sys_log` VALUES (130, 1, '查看角色树', '2019-06-19 09:49:48');
INSERT INTO `sys_log` VALUES (131, 1, '改变角色状态', '2019-06-19 09:49:57');
INSERT INTO `sys_log` VALUES (132, 1, '查看角色树', '2019-06-19 09:49:58');
INSERT INTO `sys_log` VALUES (133, 1, '查看角色树', '2019-06-19 09:50:00');
INSERT INTO `sys_log` VALUES (134, 1, '新增角色', '2019-06-19 09:50:08');
INSERT INTO `sys_log` VALUES (135, 1, '查看角色树', '2019-06-19 09:50:09');
INSERT INTO `sys_log` VALUES (136, 1, '新增角色功能', '2019-06-19 09:50:17');
INSERT INTO `sys_log` VALUES (137, 1, '删除角色', '2019-06-19 09:50:37');
INSERT INTO `sys_log` VALUES (138, 1, '查看角色树', '2019-06-19 09:50:37');
INSERT INTO `sys_log` VALUES (139, 1, '查看用户列表', '2019-06-19 09:50:38');
INSERT INTO `sys_log` VALUES (140, 1, '更新用户', '2019-06-19 09:50:55');
INSERT INTO `sys_log` VALUES (141, 1, '更新用户', '2019-06-19 09:52:05');
INSERT INTO `sys_log` VALUES (142, 1, '更新用户', '2019-06-19 09:53:01');
INSERT INTO `sys_log` VALUES (143, 1, '更新用户', '2019-06-19 09:54:07');
INSERT INTO `sys_log` VALUES (144, 1, '查看用户列表', '2019-06-19 09:54:09');
INSERT INTO `sys_log` VALUES (145, 1, '更新用户', '2019-06-19 09:54:17');
INSERT INTO `sys_log` VALUES (146, 1, '查看用户列表', '2019-06-19 09:54:17');
INSERT INTO `sys_log` VALUES (147, 1, '重置用户密码', '2019-06-19 09:54:26');
INSERT INTO `sys_log` VALUES (148, 1, '查看角色列表', '2019-06-19 09:54:29');
INSERT INTO `sys_log` VALUES (149, 1, '查看系统日志', '2019-06-19 09:54:29');
INSERT INTO `sys_log` VALUES (150, 1, '查看系统日志', '2019-06-19 09:54:42');
INSERT INTO `sys_log` VALUES (151, 1, '查看系统日志', '2019-06-19 09:54:51');
INSERT INTO `sys_log` VALUES (152, 1, '查看系统日志', '2019-06-19 09:54:53');
INSERT INTO `sys_log` VALUES (153, 1, '查看系统日志', '2019-06-19 09:54:56');
INSERT INTO `sys_log` VALUES (154, 1, '查看系统日志', '2019-06-19 09:54:58');
INSERT INTO `sys_log` VALUES (155, 1, '查看用户列表', '2019-06-19 11:01:44');
INSERT INTO `sys_log` VALUES (156, 1, '查看角色树', '2019-06-19 11:01:45');
INSERT INTO `sys_log` VALUES (157, 1, '查看功能列表', '2019-06-19 11:01:45');
INSERT INTO `sys_log` VALUES (158, 1, '查看功能列表', '2019-06-19 11:01:46');
INSERT INTO `sys_log` VALUES (159, 1, '查看角色树', '2019-06-19 11:01:47');
INSERT INTO `sys_log` VALUES (160, 1, '查看功能列表', '2019-06-19 11:01:47');
INSERT INTO `sys_log` VALUES (161, 1, '查看功能列表', '2019-06-19 11:01:48');
INSERT INTO `sys_log` VALUES (162, 1, '查看角色树', '2019-06-19 11:01:49');
INSERT INTO `sys_log` VALUES (163, 1, '查看功能列表', '2019-06-19 11:01:49');
INSERT INTO `sys_log` VALUES (164, 1, '查看功能列表', '2019-06-19 11:01:50');
INSERT INTO `sys_log` VALUES (165, 1, '查看系统日志', '2019-06-19 11:04:23');
INSERT INTO `sys_log` VALUES (166, 1, '查看角色列表', '2019-06-19 11:04:23');
INSERT INTO `sys_log` VALUES (167, 1, '查看用户列表', '2019-06-19 11:04:25');
INSERT INTO `sys_log` VALUES (168, 1, '查看角色树', '2019-06-19 11:04:26');
INSERT INTO `sys_log` VALUES (169, 1, '查看功能列表', '2019-06-19 11:04:26');
INSERT INTO `sys_log` VALUES (170, 1, '查看系统日志', '2019-06-19 11:04:27');
INSERT INTO `sys_log` VALUES (171, 1, '查看角色列表', '2019-06-19 11:04:27');
INSERT INTO `sys_log` VALUES (172, 1, '查看角色树', '2019-06-19 11:04:28');
INSERT INTO `sys_log` VALUES (173, 1, '查看功能列表', '2019-06-19 11:04:28');
INSERT INTO `sys_log` VALUES (174, 1, '查看功能列表', '2019-06-19 11:04:30');
INSERT INTO `sys_log` VALUES (175, 1, '查看角色树', '2019-06-19 11:04:30');
INSERT INTO `sys_log` VALUES (176, 1, '查看功能列表', '2019-06-19 11:04:30');
INSERT INTO `sys_log` VALUES (177, 1, '查看功能列表', '2019-06-19 11:04:32');
INSERT INTO `sys_log` VALUES (178, 1, '查看角色树', '2019-06-19 11:04:33');
INSERT INTO `sys_log` VALUES (179, 1, '查看功能列表', '2019-06-19 11:04:33');
INSERT INTO `sys_log` VALUES (180, 1, '查看系统日志', '2019-06-19 11:04:34');
INSERT INTO `sys_log` VALUES (181, 1, '查看角色列表', '2019-06-19 11:04:34');
INSERT INTO `sys_log` VALUES (182, 1, '查看功能列表', '2019-06-19 14:06:43');
INSERT INTO `sys_log` VALUES (183, 1, '查看功能列表', '2019-06-19 14:26:09');
INSERT INTO `sys_log` VALUES (184, 1, '查看功能列表', '2019-06-19 14:26:16');
INSERT INTO `sys_log` VALUES (185, 1, '查看功能列表', '2019-06-19 14:27:56');
INSERT INTO `sys_log` VALUES (186, 1, '查看功能列表', '2019-06-24 14:45:09');
INSERT INTO `sys_log` VALUES (187, 1, '查看角色树', '2019-06-24 14:45:10');
INSERT INTO `sys_log` VALUES (188, 1, '查看功能列表', '2019-06-24 14:45:10');
INSERT INTO `sys_log` VALUES (189, 1, '查看用户列表', '2019-06-24 14:45:12');
INSERT INTO `sys_log` VALUES (190, 1, '查看角色列表', '2019-06-24 14:45:13');
INSERT INTO `sys_log` VALUES (191, 1, '查看系统日志', '2019-06-24 14:45:13');
INSERT INTO `sys_log` VALUES (192, 1, '查看角色树', '2019-06-24 14:45:14');
INSERT INTO `sys_log` VALUES (193, 1, '查看功能列表', '2019-06-24 14:45:14');
INSERT INTO `sys_log` VALUES (194, 1, '查看功能列表', '2019-06-24 14:45:15');
INSERT INTO `sys_log` VALUES (195, 1, '查看功能列表', '2019-06-25 11:28:47');
INSERT INTO `sys_log` VALUES (196, 1, '查看功能列表', '2019-06-25 11:28:48');
INSERT INTO `sys_log` VALUES (197, 1, '查看角色树', '2019-06-25 11:28:48');
INSERT INTO `sys_log` VALUES (198, 1, '查看功能列表', '2019-06-25 11:28:50');
INSERT INTO `sys_log` VALUES (199, 1, '查看用户列表', '2019-06-25 11:28:54');
INSERT INTO `sys_log` VALUES (200, 1, '查看系统日志', '2019-06-25 11:28:56');
INSERT INTO `sys_log` VALUES (201, 1, '查看角色列表', '2019-06-25 11:28:56');
INSERT INTO `sys_log` VALUES (202, 1, '查看用户列表', '2019-06-25 11:28:57');
INSERT INTO `sys_log` VALUES (203, 1, '查看系统日志', '2019-06-25 11:28:58');
INSERT INTO `sys_log` VALUES (204, 1, '查看角色列表', '2019-06-25 11:28:58');
INSERT INTO `sys_log` VALUES (205, 1, '查看功能列表', '2019-06-25 11:29:01');
INSERT INTO `sys_log` VALUES (206, 1, '查看角色树', '2019-06-25 11:31:28');
INSERT INTO `sys_log` VALUES (207, 1, '查看功能列表', '2019-06-25 11:31:28');
INSERT INTO `sys_log` VALUES (208, 1, '查看功能列表', '2019-06-25 11:31:34');
INSERT INTO `sys_log` VALUES (209, 1, '查看角色树', '2019-06-25 11:31:35');
INSERT INTO `sys_log` VALUES (210, 1, '查看功能列表', '2019-06-25 11:31:35');
INSERT INTO `sys_log` VALUES (211, 1, '查看用户列表', '2019-06-25 11:31:36');
INSERT INTO `sys_log` VALUES (212, 1, '查看角色列表', '2019-06-25 11:31:36');
INSERT INTO `sys_log` VALUES (213, 1, '查看系统日志', '2019-06-25 11:31:36');
INSERT INTO `sys_log` VALUES (214, 1, '查看功能列表', '2019-06-25 11:31:37');
INSERT INTO `sys_log` VALUES (215, 1, '查看用户列表', '2019-06-25 11:31:39');
INSERT INTO `sys_log` VALUES (216, 1, '查看角色树', '2019-06-25 11:31:39');
INSERT INTO `sys_log` VALUES (217, 1, '查看功能列表', '2019-06-25 11:31:39');
INSERT INTO `sys_log` VALUES (218, 1, '查看系统日志', '2019-06-25 11:31:40');
INSERT INTO `sys_log` VALUES (219, 1, '查看角色列表', '2019-06-25 11:31:40');
INSERT INTO `sys_log` VALUES (220, 1, '查看功能列表', '2019-06-25 11:31:44');
INSERT INTO `sys_log` VALUES (221, 1, '查看功能列表', '2019-06-28 08:53:22');
INSERT INTO `sys_log` VALUES (222, 1, '查看功能列表', '2019-06-28 08:54:38');
INSERT INTO `sys_log` VALUES (223, 1, '查看角色树', '2019-06-28 08:54:58');
INSERT INTO `sys_log` VALUES (224, 1, '查看功能列表', '2019-06-28 08:54:58');
INSERT INTO `sys_log` VALUES (225, 1, '查看系统日志', '2019-06-28 08:54:59');
INSERT INTO `sys_log` VALUES (226, 1, '查看角色列表', '2019-06-28 08:54:59');
INSERT INTO `sys_log` VALUES (227, 1, '查看用户列表', '2019-06-28 08:55:05');
INSERT INTO `sys_log` VALUES (228, 1, '查看角色列表', '2019-06-28 08:55:11');
INSERT INTO `sys_log` VALUES (229, 1, '查看系统日志', '2019-06-28 08:55:11');
INSERT INTO `sys_log` VALUES (230, 1, '查看用户列表', '2019-06-28 08:55:26');
INSERT INTO `sys_log` VALUES (231, 1, '查看功能列表', '2019-06-28 09:09:59');
INSERT INTO `sys_log` VALUES (232, 1, '查看角色树', '2019-06-28 09:10:04');
INSERT INTO `sys_log` VALUES (233, 1, '查看功能列表', '2019-06-28 09:10:04');
INSERT INTO `sys_log` VALUES (234, 1, '查看用户列表', '2019-06-28 09:10:06');
INSERT INTO `sys_log` VALUES (235, 1, '查看角色列表', '2019-06-28 09:10:10');
INSERT INTO `sys_log` VALUES (236, 1, '查看系统日志', '2019-06-28 09:10:10');
INSERT INTO `sys_log` VALUES (237, 1, '查看用户列表', '2019-06-28 09:10:11');
INSERT INTO `sys_log` VALUES (238, 1, '查看功能列表', '2019-06-28 09:10:18');
INSERT INTO `sys_log` VALUES (239, 1, '查看角色树', '2019-06-28 09:10:18');
INSERT INTO `sys_log` VALUES (240, 1, '查看用户列表', '2019-06-28 09:10:19');
INSERT INTO `sys_log` VALUES (241, 1, '查看角色树', '2019-06-28 09:10:20');
INSERT INTO `sys_log` VALUES (242, 1, '查看功能列表', '2019-06-28 09:10:20');
INSERT INTO `sys_log` VALUES (243, 1, '查看功能列表', '2019-06-28 09:10:21');
INSERT INTO `sys_log` VALUES (244, 1, '查看用户列表', '2019-06-28 09:10:24');
INSERT INTO `sys_log` VALUES (245, 1, '查看角色树', '2019-06-28 09:10:30');
INSERT INTO `sys_log` VALUES (246, 1, '查看角色列表', '2019-06-28 09:10:37');
INSERT INTO `sys_log` VALUES (247, 1, '查看系统日志', '2019-06-28 09:10:37');
INSERT INTO `sys_log` VALUES (248, 1, '查看用户列表', '2019-06-28 09:10:39');
INSERT INTO `sys_log` VALUES (249, 1, '查看角色树', '2019-06-28 09:10:40');
INSERT INTO `sys_log` VALUES (250, 1, '查看功能列表', '2019-06-28 09:10:40');
INSERT INTO `sys_log` VALUES (251, 1, '查看功能列表', '2019-06-28 09:10:42');
INSERT INTO `sys_log` VALUES (252, 1, '查看用户列表', '2019-06-28 09:10:52');
INSERT INTO `sys_log` VALUES (253, 1, '查看角色列表', '2019-06-28 09:10:56');
INSERT INTO `sys_log` VALUES (254, 1, '查看系统日志', '2019-06-28 09:10:56');
INSERT INTO `sys_log` VALUES (255, 1, '查看角色树', '2019-06-28 09:10:58');
INSERT INTO `sys_log` VALUES (256, 1, '查看功能列表', '2019-06-28 09:10:58');
INSERT INTO `sys_log` VALUES (257, 1, '查看功能列表', '2019-06-28 09:11:00');
INSERT INTO `sys_log` VALUES (258, 1, '系统用户退出登录', '2019-06-28 09:11:07');
INSERT INTO `sys_log` VALUES (259, 1, '查看功能列表', '2019-06-28 09:19:10');
INSERT INTO `sys_log` VALUES (260, 1, '查看角色树', '2019-06-28 09:19:14');
INSERT INTO `sys_log` VALUES (261, 1, '查看功能列表', '2019-06-28 09:19:14');
INSERT INTO `sys_log` VALUES (262, 1, '查看用户列表', '2019-06-28 09:19:17');
INSERT INTO `sys_log` VALUES (263, 1, '查看角色列表', '2019-06-28 09:19:18');
INSERT INTO `sys_log` VALUES (264, 1, '查看系统日志', '2019-06-28 09:19:18');
INSERT INTO `sys_log` VALUES (265, 1, '查看用户列表', '2019-06-28 09:19:25');
INSERT INTO `sys_log` VALUES (266, 1, '查看角色树', '2019-06-28 09:19:31');
INSERT INTO `sys_log` VALUES (267, 1, '查看功能列表', '2019-06-28 09:19:31');
INSERT INTO `sys_log` VALUES (268, 1, '查看功能列表', '2019-06-28 09:19:36');
INSERT INTO `sys_log` VALUES (269, 1, '查看角色树', '2019-06-28 09:19:38');
INSERT INTO `sys_log` VALUES (270, 1, '查看功能列表', '2019-06-28 09:19:38');
INSERT INTO `sys_log` VALUES (271, 1, '查看用户列表', '2019-06-28 09:19:40');
INSERT INTO `sys_log` VALUES (272, 1, '查看系统日志', '2019-06-28 09:19:41');
INSERT INTO `sys_log` VALUES (273, 1, '查看角色列表', '2019-06-28 09:19:41');
INSERT INTO `sys_log` VALUES (274, 1, '查看用户列表', '2019-06-28 09:20:17');
INSERT INTO `sys_log` VALUES (275, 1, '查看角色树', '2019-06-28 09:20:18');
INSERT INTO `sys_log` VALUES (276, 1, '查看功能列表', '2019-06-28 09:20:18');
INSERT INTO `sys_log` VALUES (277, 1, '查看功能列表', '2019-06-28 09:20:20');
INSERT INTO `sys_log` VALUES (278, 1, '查看角色树', '2019-06-28 09:20:22');
INSERT INTO `sys_log` VALUES (279, 1, '查看功能列表', '2019-06-28 09:20:22');
INSERT INTO `sys_log` VALUES (280, 1, '查看用户列表', '2019-06-28 09:20:23');
INSERT INTO `sys_log` VALUES (281, 1, '查看系统日志', '2019-06-28 09:20:25');
INSERT INTO `sys_log` VALUES (282, 1, '查看角色列表', '2019-06-28 09:20:25');
INSERT INTO `sys_log` VALUES (283, 1, '查看系统日志', '2019-06-28 09:20:28');
INSERT INTO `sys_log` VALUES (284, 1, '修改密码', '2019-06-28 17:10:05');
INSERT INTO `sys_log` VALUES (285, 1, '修改密码', '2019-06-28 17:11:11');
INSERT INTO `sys_log` VALUES (286, 1, '修改密码', '2019-06-28 17:12:30');
INSERT INTO `sys_log` VALUES (287, 1, '修改密码', '2019-06-28 17:12:46');
INSERT INTO `sys_log` VALUES (288, 1, '查看功能列表', '2019-06-28 17:13:37');
INSERT INTO `sys_log` VALUES (289, 1, '查看角色树', '2019-06-28 17:13:39');
INSERT INTO `sys_log` VALUES (290, 1, '查看功能列表', '2019-06-28 17:13:39');
INSERT INTO `sys_log` VALUES (291, 1, '查看用户列表', '2019-06-28 17:13:40');
INSERT INTO `sys_log` VALUES (292, 1, '查看系统日志', '2019-06-28 17:13:41');
INSERT INTO `sys_log` VALUES (293, 1, '查看角色列表', '2019-06-28 17:13:41');
INSERT INTO `sys_log` VALUES (294, 1, '查看用户列表', '2019-06-28 17:13:43');
INSERT INTO `sys_log` VALUES (295, 1, '更新用户', '2019-06-28 17:13:47');
INSERT INTO `sys_log` VALUES (296, 1, '查看用户列表', '2019-06-28 17:13:48');
INSERT INTO `sys_log` VALUES (297, 1, '更新用户', '2019-06-28 17:13:52');
INSERT INTO `sys_log` VALUES (298, 1, '查看用户列表', '2019-06-28 17:13:52');
INSERT INTO `sys_log` VALUES (299, 1, '查看用户列表', '2019-06-28 17:14:27');
INSERT INTO `sys_log` VALUES (300, 1, '查看用户列表', '2019-06-28 17:14:55');
INSERT INTO `sys_log` VALUES (301, 1, '新增用户', '2019-06-28 17:17:46');
INSERT INTO `sys_log` VALUES (302, 1, '新增用户', '2019-06-28 17:18:23');
INSERT INTO `sys_log` VALUES (303, 1, '查看用户列表', '2019-06-28 17:18:24');
INSERT INTO `sys_log` VALUES (304, 1, '系统用户退出登录', '2019-06-28 17:18:34');
INSERT INTO `sys_log` VALUES (305, 1, '查看用户列表', '2019-06-28 17:19:16');
INSERT INTO `sys_log` VALUES (306, 1, '查看角色树', '2019-06-28 17:19:18');
INSERT INTO `sys_log` VALUES (307, 1, '更新用户角色', '2019-06-28 17:19:20');
INSERT INTO `sys_log` VALUES (308, 1, '查看用户列表', '2019-06-28 17:19:20');
INSERT INTO `sys_log` VALUES (309, 1, '查看用户列表', '2019-06-28 17:19:22');
INSERT INTO `sys_log` VALUES (310, 1, '更新用户角色', '2019-06-28 17:19:37');
INSERT INTO `sys_log` VALUES (311, 1, '查看用户列表', '2019-06-28 17:19:37');
INSERT INTO `sys_log` VALUES (312, 1000003, '系统用户退出登录', '2019-06-28 17:21:06');
INSERT INTO `sys_log` VALUES (313, 1, '查看用户列表', '2019-07-01 14:26:54');
INSERT INTO `sys_log` VALUES (314, 1, '查看功能列表', '2019-07-01 15:52:55');
INSERT INTO `sys_log` VALUES (315, 1, '新增功能', '2019-07-01 15:53:54');
INSERT INTO `sys_log` VALUES (316, 1, '查看功能列表', '2019-07-01 15:53:55');
INSERT INTO `sys_log` VALUES (317, 1, '新增功能', '2019-07-01 15:56:32');
INSERT INTO `sys_log` VALUES (318, 1, '查看功能列表', '2019-07-01 15:56:32');
INSERT INTO `sys_log` VALUES (319, 1, '查看角色树', '2019-07-01 15:56:34');
INSERT INTO `sys_log` VALUES (320, 1, '查看功能列表', '2019-07-01 15:56:34');
INSERT INTO `sys_log` VALUES (321, 1, '新增角色功能', '2019-07-01 15:56:37');
INSERT INTO `sys_log` VALUES (322, 1, '系统用户退出登录', '2019-07-01 15:56:40');
INSERT INTO `sys_log` VALUES (323, 1, '查看角色树', '2019-07-01 17:22:26');
INSERT INTO `sys_log` VALUES (324, 1, '查看功能列表', '2019-07-01 17:22:26');
INSERT INTO `sys_log` VALUES (325, 1, '查看功能列表', '2019-07-01 17:22:28');
INSERT INTO `sys_log` VALUES (326, 1, '查看功能列表', '2019-07-19 11:00:08');
INSERT INTO `sys_log` VALUES (327, 1, '查看角色列表', '2019-07-19 11:00:11');
INSERT INTO `sys_log` VALUES (328, 1, '查看系统日志', '2019-07-19 11:00:11');
INSERT INTO `sys_log` VALUES (329, 1, '查看系统日志', '2019-07-19 11:05:49');
INSERT INTO `sys_log` VALUES (330, 1, '查看角色列表', '2019-07-19 11:05:49');
INSERT INTO `sys_log` VALUES (331, 1, '查看系统日志', '2019-07-19 11:10:59');
INSERT INTO `sys_log` VALUES (332, 1, '查看系统日志', '2019-07-19 11:12:04');
INSERT INTO `sys_log` VALUES (333, 1, '查看系统日志', '2019-07-19 11:15:11');
INSERT INTO `sys_log` VALUES (334, 1, '查看系统日志', '2019-07-19 11:15:55');
INSERT INTO `sys_log` VALUES (335, 1, '查看系统日志', '2019-07-19 11:22:37');
INSERT INTO `sys_log` VALUES (336, 1, '查看系统日志', '2019-07-19 11:47:40');
INSERT INTO `sys_log` VALUES (337, 1, '查看系统日志', '2019-07-19 11:48:17');
INSERT INTO `sys_log` VALUES (338, 1, '查看系统日志', '2019-07-19 11:49:13');
INSERT INTO `sys_log` VALUES (339, 1, '查看角色列表', '2019-07-19 11:49:31');
INSERT INTO `sys_log` VALUES (340, 1, '查看系统日志', '2019-07-19 11:49:32');
INSERT INTO `sys_log` VALUES (341, 1, '查看用户列表', '2019-07-19 11:50:50');
INSERT INTO `sys_log` VALUES (342, 1, '查看角色列表', '2019-07-19 11:50:51');
INSERT INTO `sys_log` VALUES (343, 1, '查看系统日志', '2019-07-19 11:50:51');
INSERT INTO `sys_log` VALUES (344, 1, '查看系统日志', '2019-07-19 11:51:05');
INSERT INTO `sys_log` VALUES (345, 1, '查看角色列表', '2019-07-19 11:51:05');
INSERT INTO `sys_log` VALUES (346, 1, '查看功能列表', '2019-07-19 15:09:43');
INSERT INTO `sys_log` VALUES (347, 1, '查看角色树', '2019-07-19 15:09:51');
INSERT INTO `sys_log` VALUES (348, 1, '查看功能列表', '2019-07-19 15:09:51');
INSERT INTO `sys_log` VALUES (349, 1, '查看用户列表', '2019-07-19 15:09:54');
INSERT INTO `sys_log` VALUES (350, 1, '查看角色列表', '2019-07-19 15:10:10');
INSERT INTO `sys_log` VALUES (351, 1, '查看系统日志', '2019-07-19 15:10:10');
INSERT INTO `sys_log` VALUES (352, 1, '查看功能列表', '2019-07-23 11:27:33');
INSERT INTO `sys_log` VALUES (353, 1, '查看功能列表', '2019-07-23 11:27:39');
INSERT INTO `sys_log` VALUES (354, 1, '查看角色树', '2019-07-23 11:27:39');
INSERT INTO `sys_log` VALUES (355, 1, '查看用户列表', '2019-07-23 11:27:40');
INSERT INTO `sys_log` VALUES (356, 1, '查看角色列表', '2019-07-23 11:27:41');
INSERT INTO `sys_log` VALUES (357, 1, '查看系统日志', '2019-07-23 11:27:41');
INSERT INTO `sys_log` VALUES (358, 1, '查看角色树', '2019-07-23 11:27:42');
INSERT INTO `sys_log` VALUES (359, 1, '查看功能列表', '2019-07-23 11:27:42');
INSERT INTO `sys_log` VALUES (360, 1, '查看用户列表', '2019-07-23 11:27:44');
INSERT INTO `sys_log` VALUES (361, 1, '查看功能列表', '2019-07-23 11:27:45');
INSERT INTO `sys_log` VALUES (362, 1, '删除功能', '2019-07-23 11:27:52');
INSERT INTO `sys_log` VALUES (363, 1, '删除功能', '2019-07-23 11:27:59');
INSERT INTO `sys_log` VALUES (364, 1, '查看功能列表', '2019-07-23 11:27:59');
INSERT INTO `sys_log` VALUES (365, 1, '删除功能', '2019-07-23 11:28:04');
INSERT INTO `sys_log` VALUES (366, 1, '查看功能列表', '2019-07-23 11:28:04');
INSERT INTO `sys_log` VALUES (367, 1, '查看角色树', '2019-07-23 11:28:06');
INSERT INTO `sys_log` VALUES (368, 1, '查看功能列表', '2019-07-23 11:28:06');
INSERT INTO `sys_log` VALUES (369, 1, '查看用户列表', '2019-07-23 11:28:09');
INSERT INTO `sys_log` VALUES (370, 1, '查看系统日志', '2019-07-23 11:28:11');
INSERT INTO `sys_log` VALUES (371, 1, '查看角色列表', '2019-07-23 11:28:11');
INSERT INTO `sys_log` VALUES (372, 1, '查看功能列表', '2019-07-23 11:28:13');
INSERT INTO `sys_log` VALUES (373, 1, '查看角色树', '2019-07-23 11:28:14');
INSERT INTO `sys_log` VALUES (374, 1, '查看功能列表', '2019-07-23 11:28:14');
INSERT INTO `sys_log` VALUES (375, 1, '查看功能列表', '2019-07-23 11:33:36');
INSERT INTO `sys_log` VALUES (376, 1, '新增功能', '2019-07-23 11:33:46');
INSERT INTO `sys_log` VALUES (377, 1, '查看功能列表', '2019-07-23 11:33:46');
INSERT INTO `sys_log` VALUES (378, 1, '修改功能', '2019-07-23 11:33:51');
INSERT INTO `sys_log` VALUES (379, 1, '修改功能', '2019-07-23 11:35:49');
INSERT INTO `sys_log` VALUES (380, 1, '查看功能列表', '2019-07-23 11:35:50');
INSERT INTO `sys_log` VALUES (381, 1, '删除功能', '2019-07-23 11:35:58');
INSERT INTO `sys_log` VALUES (382, 1, '查看功能列表', '2019-07-23 11:35:59');
INSERT INTO `sys_log` VALUES (383, 1, '查看角色树', '2019-07-23 11:36:00');
INSERT INTO `sys_log` VALUES (384, 1, '查看功能列表', '2019-07-23 11:36:00');
INSERT INTO `sys_log` VALUES (385, 1, '新增角色功能', '2019-07-23 11:36:03');
INSERT INTO `sys_log` VALUES (386, 1, '新增角色功能', '2019-07-23 11:36:05');
INSERT INTO `sys_log` VALUES (387, 1, '新增角色', '2019-07-23 11:36:14');
INSERT INTO `sys_log` VALUES (388, 1, '查看角色树', '2019-07-23 11:36:14');
INSERT INTO `sys_log` VALUES (389, 1, '删除角色', '2019-07-23 11:36:19');
INSERT INTO `sys_log` VALUES (390, 1, '查看角色树', '2019-07-23 11:36:19');
INSERT INTO `sys_log` VALUES (391, 1, '查看用户列表', '2019-07-23 11:36:21');
INSERT INTO `sys_log` VALUES (392, 1, '更新用户', '2019-07-23 11:36:27');
INSERT INTO `sys_log` VALUES (393, 1, '查看用户列表', '2019-07-23 11:36:27');
INSERT INTO `sys_log` VALUES (394, 1, '查看角色列表', '2019-07-23 11:36:38');
INSERT INTO `sys_log` VALUES (395, 1, '查看系统日志', '2019-07-23 11:36:38');
INSERT INTO `sys_log` VALUES (396, 1, '查看功能列表', '2019-07-23 11:36:41');
INSERT INTO `sys_log` VALUES (397, 1, '查看功能列表', '2019-07-23 14:21:45');
INSERT INTO `sys_log` VALUES (398, 1, '查看角色树', '2019-07-23 14:21:47');
INSERT INTO `sys_log` VALUES (399, 1, '查看功能列表', '2019-07-23 14:21:47');
INSERT INTO `sys_log` VALUES (400, 1, '查看系统日志', '2019-07-23 14:21:49');
INSERT INTO `sys_log` VALUES (401, 1, '查看角色列表', '2019-07-23 14:21:49');
INSERT INTO `sys_log` VALUES (402, 1, '查看用户列表', '2019-07-23 14:21:50');
INSERT INTO `sys_log` VALUES (403, 1, '查看功能列表', '2019-07-23 14:21:54');
INSERT INTO `sys_log` VALUES (404, 1, '查看角色列表', '2019-08-01 14:33:13');
INSERT INTO `sys_log` VALUES (405, 1, '查看系统日志', '2019-08-01 14:33:14');
INSERT INTO `sys_log` VALUES (406, 1, '查看用户列表', '2019-08-01 14:33:15');
INSERT INTO `sys_log` VALUES (407, 1, '查看角色树', '2019-08-01 14:33:18');
INSERT INTO `sys_log` VALUES (408, 1, '查看用户列表', '2019-08-01 15:04:37');
INSERT INTO `sys_log` VALUES (409, 1, '查看用户列表', '2019-08-01 15:05:02');
INSERT INTO `sys_log` VALUES (410, 1, '查看角色树', '2019-08-01 15:05:41');
INSERT INTO `sys_log` VALUES (411, 1, '查看功能列表', '2019-08-01 15:05:41');
INSERT INTO `sys_log` VALUES (412, 1, '查看角色树', '2019-08-01 15:06:12');
INSERT INTO `sys_log` VALUES (413, 1, '查看用户列表', '2019-08-01 15:08:18');
INSERT INTO `sys_log` VALUES (414, 1, '查看功能列表', '2019-08-01 15:08:19');
INSERT INTO `sys_log` VALUES (415, 1, '查看角色树', '2019-08-01 15:08:19');
INSERT INTO `sys_log` VALUES (416, 1, '查看角色树', '2019-08-01 15:08:19');
INSERT INTO `sys_log` VALUES (417, 1, '查看功能列表', '2019-08-01 15:08:19');
INSERT INTO `sys_log` VALUES (418, 1, '查看角色树', '2019-08-01 15:08:37');
INSERT INTO `sys_log` VALUES (419, 1, '查看功能列表', '2019-08-01 15:08:37');
INSERT INTO `sys_log` VALUES (420, 1, '查看角色树', '2019-08-01 15:09:33');
INSERT INTO `sys_log` VALUES (421, 1, '查看功能列表', '2019-08-01 15:09:33');
INSERT INTO `sys_log` VALUES (422, 1, '查看用户列表', '2019-08-01 15:13:03');
INSERT INTO `sys_log` VALUES (423, 1, '查看用户列表', '2019-08-01 15:14:50');
INSERT INTO `sys_log` VALUES (424, 1, '查看用户列表', '2019-08-01 15:25:19');
INSERT INTO `sys_log` VALUES (425, 1, '查看用户列表', '2019-08-01 15:26:31');
INSERT INTO `sys_log` VALUES (426, 1, '查看用户列表', '2019-08-01 15:35:44');
INSERT INTO `sys_log` VALUES (427, 1, '查看用户列表', '2019-08-01 15:35:58');
INSERT INTO `sys_log` VALUES (428, 1, '查看用户列表', '2019-08-01 15:37:01');
INSERT INTO `sys_log` VALUES (429, 1, '查看用户列表', '2019-08-01 15:37:21');
INSERT INTO `sys_log` VALUES (430, 1, '查看用户列表', '2019-08-01 15:37:47');
INSERT INTO `sys_log` VALUES (431, 1, '查看用户列表', '2019-08-01 15:38:30');
INSERT INTO `sys_log` VALUES (432, 1, '查看用户列表', '2019-08-01 15:39:23');
INSERT INTO `sys_log` VALUES (433, 1, '查看用户列表', '2019-08-01 15:39:41');
INSERT INTO `sys_log` VALUES (434, 1, '查看用户列表', '2019-08-01 15:40:48');
INSERT INTO `sys_log` VALUES (435, 1, '查看用户列表', '2019-08-01 15:41:13');
INSERT INTO `sys_log` VALUES (436, 1, '解除封号', '2019-08-01 15:41:18');
INSERT INTO `sys_log` VALUES (437, 1, '查看用户列表', '2019-08-01 15:41:19');
INSERT INTO `sys_log` VALUES (438, 1, '查看用户列表', '2019-08-01 15:41:31');
INSERT INTO `sys_log` VALUES (439, 1, '查看用户列表', '2019-08-01 15:41:33');
INSERT INTO `sys_log` VALUES (440, 1, '查看用户列表', '2019-08-01 15:41:33');
INSERT INTO `sys_log` VALUES (441, 1, '查看用户列表', '2019-08-01 15:41:33');
INSERT INTO `sys_log` VALUES (442, 1, '查看用户列表', '2019-08-01 15:41:33');
INSERT INTO `sys_log` VALUES (443, 1, '查看用户列表', '2019-08-01 15:41:34');
INSERT INTO `sys_log` VALUES (444, 1, '解除封号', '2019-08-01 15:41:55');
INSERT INTO `sys_log` VALUES (445, 1, '查看用户列表', '2019-08-01 15:41:55');
INSERT INTO `sys_log` VALUES (446, 1, '查看用户列表', '2019-08-01 15:42:24');
INSERT INTO `sys_log` VALUES (447, 1, '解除封号', '2019-08-01 15:42:27');
INSERT INTO `sys_log` VALUES (448, 1, '查看用户列表', '2019-08-01 15:42:27');
INSERT INTO `sys_log` VALUES (449, 1, '解除封号', '2019-08-01 15:43:17');
INSERT INTO `sys_log` VALUES (450, 1, '查看用户列表', '2019-08-01 15:43:24');
INSERT INTO `sys_log` VALUES (451, 1, '查看用户列表', '2019-08-01 15:44:27');
INSERT INTO `sys_log` VALUES (452, 1, '解除封号', '2019-08-01 15:44:30');
INSERT INTO `sys_log` VALUES (453, 1, '查看用户列表', '2019-08-01 15:44:30');
INSERT INTO `sys_log` VALUES (454, 1, '系统用户退出登录', '2019-08-01 15:44:40');
INSERT INTO `sys_log` VALUES (455, 666, '系统用户退出登录', '2019-08-01 15:47:01');
INSERT INTO `sys_log` VALUES (456, 666, '查看用户列表', '2019-08-01 15:49:01');
INSERT INTO `sys_log` VALUES (457, 666, '查看用户列表', '2019-08-01 15:50:54');
INSERT INTO `sys_log` VALUES (458, 666, '查看用户列表', '2019-08-01 15:53:51');
INSERT INTO `sys_log` VALUES (459, 666, '查看用户列表', '2019-08-01 15:54:21');
INSERT INTO `sys_log` VALUES (460, 666, '查看用户列表', '2019-08-01 15:55:34');
INSERT INTO `sys_log` VALUES (461, 666, '查看用户列表', '2019-08-01 15:56:20');
INSERT INTO `sys_log` VALUES (462, 666, '查看用户列表', '2019-08-01 15:56:29');
INSERT INTO `sys_log` VALUES (463, 666, '查看用户列表', '2019-08-01 15:56:38');
INSERT INTO `sys_log` VALUES (464, 666, '查看用户列表', '2019-08-01 15:57:01');
INSERT INTO `sys_log` VALUES (465, 666, '查看用户列表', '2019-08-01 15:58:58');
INSERT INTO `sys_log` VALUES (466, 666, '系统用户退出登录', '2019-08-01 15:59:23');
INSERT INTO `sys_log` VALUES (467, 1, '查看用户列表', '2019-08-01 16:00:14');
INSERT INTO `sys_log` VALUES (468, 1, '查看角色树', '2019-08-01 16:00:19');
INSERT INTO `sys_log` VALUES (469, 1, '查看功能列表', '2019-08-01 16:00:19');
INSERT INTO `sys_log` VALUES (470, 1, '查看系统日志', '2019-08-01 16:00:21');
INSERT INTO `sys_log` VALUES (471, 1, '查看角色列表', '2019-08-01 16:00:21');
INSERT INTO `sys_log` VALUES (472, 1, '查看功能列表', '2019-08-01 16:00:26');
INSERT INTO `sys_log` VALUES (473, 1, '查看功能列表', '2019-08-01 16:00:30');
INSERT INTO `sys_log` VALUES (474, 1, '查看角色树', '2019-08-01 16:00:30');
INSERT INTO `sys_log` VALUES (475, 1, '查看用户列表', '2019-08-01 16:00:32');
INSERT INTO `sys_log` VALUES (476, 1, '查看角色树', '2019-08-01 16:00:33');
INSERT INTO `sys_log` VALUES (477, 1, '查看功能列表', '2019-08-01 16:00:33');
INSERT INTO `sys_log` VALUES (478, 1, '查看用户列表', '2019-08-01 16:00:38');
INSERT INTO `sys_log` VALUES (479, 1, '解除封号', '2019-08-01 16:00:42');
INSERT INTO `sys_log` VALUES (480, 1, '查看用户列表', '2019-08-01 16:00:43');
INSERT INTO `sys_log` VALUES (481, 1, '查看用户列表', '2019-08-01 16:01:04');
INSERT INTO `sys_log` VALUES (482, 1, '查看用户列表', '2019-08-01 16:01:56');
INSERT INTO `sys_log` VALUES (483, 1, '查看用户列表', '2019-08-01 16:03:49');
INSERT INTO `sys_log` VALUES (484, 1, '查看用户列表', '2019-08-01 16:07:44');
INSERT INTO `sys_log` VALUES (485, 1, '查看用户列表', '2019-08-01 16:07:58');
INSERT INTO `sys_log` VALUES (486, 1, '查看用户列表', '2019-08-01 16:08:05');
INSERT INTO `sys_log` VALUES (487, 1, '查看用户列表', '2019-08-01 16:08:55');
INSERT INTO `sys_log` VALUES (488, 1, '查看用户列表', '2019-08-01 16:08:57');
INSERT INTO `sys_log` VALUES (489, 1, '解除封号', '2019-08-01 16:09:04');
INSERT INTO `sys_log` VALUES (490, 1, '查看用户列表', '2019-08-01 16:09:04');
INSERT INTO `sys_log` VALUES (491, 666, '查看用户列表', '2019-08-01 16:28:00');
INSERT INTO `sys_log` VALUES (492, 666, '查看用户列表', '2019-08-01 16:28:53');
INSERT INTO `sys_log` VALUES (493, 1, '查看用户列表', '2019-08-01 16:29:18');
INSERT INTO `sys_log` VALUES (494, 1, '解除封号', '2019-08-01 16:29:20');
INSERT INTO `sys_log` VALUES (495, 1, '查看用户列表', '2019-08-01 16:29:21');
INSERT INTO `sys_log` VALUES (496, 666, '查看用户列表', '2019-08-01 16:29:41');
INSERT INTO `sys_log` VALUES (497, 666, '查看用户列表', '2019-08-01 16:30:38');
INSERT INTO `sys_log` VALUES (498, 666, '查看用户列表', '2019-08-01 16:30:51');
INSERT INTO `sys_log` VALUES (499, 666, '查看用户列表', '2019-08-01 16:30:52');
INSERT INTO `sys_log` VALUES (500, 666, '查看用户列表', '2019-08-01 16:30:52');
INSERT INTO `sys_log` VALUES (501, 666, '查看用户列表', '2019-08-01 16:30:52');
INSERT INTO `sys_log` VALUES (502, 666, '查看用户列表', '2019-08-01 16:30:52');
INSERT INTO `sys_log` VALUES (503, 666, '查看用户列表', '2019-08-01 16:30:52');
INSERT INTO `sys_log` VALUES (504, 666, '查看用户列表', '2019-08-01 16:31:00');
INSERT INTO `sys_log` VALUES (505, 666, '查看用户列表', '2019-08-01 16:31:01');
INSERT INTO `sys_log` VALUES (506, 666, '查看用户列表', '2019-08-01 16:31:01');
INSERT INTO `sys_log` VALUES (507, 666, '查看用户列表', '2019-08-01 16:31:01');
INSERT INTO `sys_log` VALUES (508, 666, '查看用户列表', '2019-08-01 16:31:01');
INSERT INTO `sys_log` VALUES (509, 666, '查看用户列表', '2019-08-01 16:31:01');
INSERT INTO `sys_log` VALUES (510, 666, '查看用户列表', '2019-08-01 16:31:02');
INSERT INTO `sys_log` VALUES (511, 666, '查看用户列表', '2019-08-01 16:31:02');
INSERT INTO `sys_log` VALUES (512, 666, '查看用户列表', '2019-08-01 16:31:02');
INSERT INTO `sys_log` VALUES (513, 666, '查看用户列表', '2019-08-01 16:31:02');
INSERT INTO `sys_log` VALUES (514, 666, '查看用户列表', '2019-08-01 16:31:02');
INSERT INTO `sys_log` VALUES (515, 666, '查看用户列表', '2019-08-01 16:31:21');
INSERT INTO `sys_log` VALUES (516, 666, '查看用户列表', '2019-08-01 16:31:21');
INSERT INTO `sys_log` VALUES (517, 666, '查看用户列表', '2019-08-01 16:31:21');
INSERT INTO `sys_log` VALUES (518, 666, '查看用户列表', '2019-08-01 16:31:21');
INSERT INTO `sys_log` VALUES (519, 666, '查看用户列表', '2019-08-01 16:31:21');
INSERT INTO `sys_log` VALUES (520, 666, '查看用户列表', '2019-08-01 16:31:32');
INSERT INTO `sys_log` VALUES (521, 666, '查看用户列表', '2019-08-01 16:31:32');
INSERT INTO `sys_log` VALUES (522, 666, '查看用户列表', '2019-08-01 16:31:33');
INSERT INTO `sys_log` VALUES (523, 666, '查看用户列表', '2019-08-01 16:31:33');
INSERT INTO `sys_log` VALUES (524, 666, '查看用户列表', '2019-08-01 16:31:33');
INSERT INTO `sys_log` VALUES (525, 666, '查看用户列表', '2019-08-01 16:31:33');
INSERT INTO `sys_log` VALUES (526, 666, '查看用户列表', '2019-08-01 16:31:33');
INSERT INTO `sys_log` VALUES (527, 666, '查看用户列表', '2019-08-01 16:31:34');
INSERT INTO `sys_log` VALUES (528, 666, '查看用户列表', '2019-08-01 16:31:34');
INSERT INTO `sys_log` VALUES (529, 666, '查看用户列表', '2019-08-01 16:31:34');
INSERT INTO `sys_log` VALUES (530, 666, '查看用户列表', '2019-08-01 16:31:34');
INSERT INTO `sys_log` VALUES (531, 666, '查看用户列表', '2019-08-01 16:31:34');
INSERT INTO `sys_log` VALUES (532, 666, '查看用户列表', '2019-08-01 16:31:34');
INSERT INTO `sys_log` VALUES (533, 666, '查看用户列表', '2019-08-01 16:31:35');
INSERT INTO `sys_log` VALUES (534, 666, '查看用户列表', '2019-08-01 16:31:35');
INSERT INTO `sys_log` VALUES (535, 666, '查看用户列表', '2019-08-01 16:31:35');
INSERT INTO `sys_log` VALUES (536, 666, '查看用户列表', '2019-08-01 16:32:09');
INSERT INTO `sys_log` VALUES (537, 666, '查看用户列表', '2019-08-01 16:32:09');
INSERT INTO `sys_log` VALUES (538, 666, '查看用户列表', '2019-08-01 16:32:10');
INSERT INTO `sys_log` VALUES (539, 666, '查看用户列表', '2019-08-01 16:32:10');
INSERT INTO `sys_log` VALUES (540, 666, '查看用户列表', '2019-08-01 16:32:10');
INSERT INTO `sys_log` VALUES (541, 666, '查看用户列表', '2019-08-01 16:32:10');
INSERT INTO `sys_log` VALUES (542, 666, '查看用户列表', '2019-08-01 16:32:10');
INSERT INTO `sys_log` VALUES (543, 666, '查看用户列表', '2019-08-01 16:32:11');
INSERT INTO `sys_log` VALUES (544, 666, '查看用户列表', '2019-08-01 16:32:11');
INSERT INTO `sys_log` VALUES (545, 666, '查看用户列表', '2019-08-01 16:32:11');
INSERT INTO `sys_log` VALUES (546, 666, '查看用户列表', '2019-08-01 16:32:11');
INSERT INTO `sys_log` VALUES (547, 666, '查看用户列表', '2019-08-01 16:32:12');
INSERT INTO `sys_log` VALUES (548, 666, '查看用户列表', '2019-08-01 16:32:12');
INSERT INTO `sys_log` VALUES (549, 666, '查看用户列表', '2019-08-01 16:32:12');
INSERT INTO `sys_log` VALUES (550, 666, '查看用户列表', '2019-08-01 16:32:12');
INSERT INTO `sys_log` VALUES (551, 666, '查看用户列表', '2019-08-01 16:32:12');
INSERT INTO `sys_log` VALUES (552, 666, '查看用户列表', '2019-08-01 16:32:12');
INSERT INTO `sys_log` VALUES (553, 666, '查看用户列表', '2019-08-01 16:32:13');
INSERT INTO `sys_log` VALUES (554, 666, '查看用户列表', '2019-08-01 16:32:13');
INSERT INTO `sys_log` VALUES (555, 666, '查看用户列表', '2019-08-01 16:32:24');
INSERT INTO `sys_log` VALUES (556, 666, '查看用户列表', '2019-08-01 16:32:25');
INSERT INTO `sys_log` VALUES (557, 666, '查看用户列表', '2019-08-01 16:32:25');
INSERT INTO `sys_log` VALUES (558, 666, '查看用户列表', '2019-08-01 16:32:25');
INSERT INTO `sys_log` VALUES (559, 666, '查看用户列表', '2019-08-01 16:32:25');
INSERT INTO `sys_log` VALUES (560, 666, '查看用户列表', '2019-08-01 16:32:25');
INSERT INTO `sys_log` VALUES (561, 666, '查看用户列表', '2019-08-01 16:32:25');
INSERT INTO `sys_log` VALUES (562, 666, '查看用户列表', '2019-08-01 16:32:26');
INSERT INTO `sys_log` VALUES (563, 666, '查看用户列表', '2019-08-01 16:32:26');
INSERT INTO `sys_log` VALUES (564, 666, '查看用户列表', '2019-08-01 16:32:26');
INSERT INTO `sys_log` VALUES (565, 666, '查看用户列表', '2019-08-01 16:32:26');
INSERT INTO `sys_log` VALUES (566, 666, '查看用户列表', '2019-08-01 16:32:26');
INSERT INTO `sys_log` VALUES (567, 666, '查看用户列表', '2019-08-01 16:32:27');
INSERT INTO `sys_log` VALUES (568, 666, '查看用户列表', '2019-08-01 16:32:27');
INSERT INTO `sys_log` VALUES (569, 666, '查看用户列表', '2019-08-01 16:32:27');
INSERT INTO `sys_log` VALUES (570, 666, '查看用户列表', '2019-08-01 16:32:27');
INSERT INTO `sys_log` VALUES (571, 666, '查看用户列表', '2019-08-01 16:32:36');
INSERT INTO `sys_log` VALUES (572, 666, '查看用户列表', '2019-08-01 16:32:36');
INSERT INTO `sys_log` VALUES (573, 666, '查看用户列表', '2019-08-01 16:32:36');
INSERT INTO `sys_log` VALUES (574, 666, '查看用户列表', '2019-08-01 16:32:36');
INSERT INTO `sys_log` VALUES (575, 666, '查看用户列表', '2019-08-01 16:32:37');
INSERT INTO `sys_log` VALUES (576, 666, '查看用户列表', '2019-08-01 16:32:37');
INSERT INTO `sys_log` VALUES (577, 666, '查看用户列表', '2019-08-01 16:32:37');
INSERT INTO `sys_log` VALUES (578, 666, '查看用户列表', '2019-08-01 16:32:43');
INSERT INTO `sys_log` VALUES (579, 666, '查看用户列表', '2019-08-01 16:32:43');
INSERT INTO `sys_log` VALUES (580, 666, '查看用户列表', '2019-08-01 16:32:44');
INSERT INTO `sys_log` VALUES (581, 666, '查看用户列表', '2019-08-01 16:32:44');
INSERT INTO `sys_log` VALUES (582, 666, '查看用户列表', '2019-08-01 16:32:44');
INSERT INTO `sys_log` VALUES (583, 666, '查看用户列表', '2019-08-01 16:32:44');
INSERT INTO `sys_log` VALUES (584, 666, '查看用户列表', '2019-08-01 16:32:49');
INSERT INTO `sys_log` VALUES (585, 666, '查看用户列表', '2019-08-01 16:32:49');
INSERT INTO `sys_log` VALUES (586, 1, '查看用户列表', '2019-08-01 16:40:54');
INSERT INTO `sys_log` VALUES (587, 1, '解除封号', '2019-08-01 16:40:58');
INSERT INTO `sys_log` VALUES (588, 1, '查看用户列表', '2019-08-01 16:40:59');
INSERT INTO `sys_log` VALUES (589, 666, '查看用户列表', '2019-08-01 16:41:13');
INSERT INTO `sys_log` VALUES (590, 666, '查看用户列表', '2019-08-01 16:41:17');
INSERT INTO `sys_log` VALUES (591, 666, '查看用户列表', '2019-08-01 16:41:18');
INSERT INTO `sys_log` VALUES (592, 666, '查看用户列表', '2019-08-01 16:41:18');
INSERT INTO `sys_log` VALUES (593, 666, '查看用户列表', '2019-08-01 16:41:18');
INSERT INTO `sys_log` VALUES (594, 666, '查看用户列表', '2019-08-01 16:41:18');
INSERT INTO `sys_log` VALUES (595, 666, '查看用户列表', '2019-08-01 16:41:38');
INSERT INTO `sys_log` VALUES (596, 666, '查看用户列表', '2019-08-01 16:41:38');
INSERT INTO `sys_log` VALUES (597, 666, '查看用户列表', '2019-08-01 16:41:38');
INSERT INTO `sys_log` VALUES (598, 666, '查看用户列表', '2019-08-01 16:42:02');
INSERT INTO `sys_log` VALUES (599, 666, '查看用户列表', '2019-08-01 16:42:02');
INSERT INTO `sys_log` VALUES (600, 666, '查看用户列表', '2019-08-01 16:42:02');
INSERT INTO `sys_log` VALUES (601, 666, '查看用户列表', '2019-08-01 16:42:02');
INSERT INTO `sys_log` VALUES (602, 666, '查看用户列表', '2019-08-01 16:42:02');
INSERT INTO `sys_log` VALUES (603, 666, '查看用户列表', '2019-08-01 16:42:18');
INSERT INTO `sys_log` VALUES (604, 666, '查看用户列表', '2019-08-01 16:42:18');
INSERT INTO `sys_log` VALUES (605, 666, '查看用户列表', '2019-08-01 16:42:19');
INSERT INTO `sys_log` VALUES (606, 666, '查看用户列表', '2019-08-01 16:42:19');
INSERT INTO `sys_log` VALUES (607, 666, '查看用户列表', '2019-08-01 16:43:24');
INSERT INTO `sys_log` VALUES (608, 666, '查看用户列表', '2019-08-01 16:43:25');
INSERT INTO `sys_log` VALUES (609, 666, '查看用户列表', '2019-08-01 16:43:30');
INSERT INTO `sys_log` VALUES (610, 666, '查看用户列表', '2019-08-01 16:43:51');
INSERT INTO `sys_log` VALUES (611, 666, '查看用户列表', '2019-08-01 16:43:51');
INSERT INTO `sys_log` VALUES (612, 666, '查看用户列表', '2019-08-01 16:43:51');
INSERT INTO `sys_log` VALUES (613, 666, '查看用户列表', '2019-08-01 16:44:46');
INSERT INTO `sys_log` VALUES (614, 666, '查看用户列表', '2019-08-01 16:45:22');
INSERT INTO `sys_log` VALUES (615, 666, '查看用户列表', '2019-08-01 16:45:31');
INSERT INTO `sys_log` VALUES (616, 666, '查看用户列表', '2019-08-01 16:53:25');
INSERT INTO `sys_log` VALUES (617, 666, '查看用户列表', '2019-08-01 16:53:25');
INSERT INTO `sys_log` VALUES (618, 666, '查看用户列表', '2019-08-01 16:53:25');
INSERT INTO `sys_log` VALUES (619, 666, '查看用户列表', '2019-08-01 16:53:25');
INSERT INTO `sys_log` VALUES (620, 666, '查看用户列表', '2019-08-01 16:53:25');
INSERT INTO `sys_log` VALUES (621, 666, '查看用户列表', '2019-08-01 16:54:08');
INSERT INTO `sys_log` VALUES (622, 666, '查看用户列表', '2019-08-01 17:01:42');
INSERT INTO `sys_log` VALUES (623, 666, '查看用户列表', '2019-08-01 17:02:03');
INSERT INTO `sys_log` VALUES (624, 666, '查看用户列表', '2019-08-01 17:05:45');
INSERT INTO `sys_log` VALUES (625, 666, '查看用户列表', '2019-08-01 17:05:45');
INSERT INTO `sys_log` VALUES (626, 666, '查看用户列表', '2019-08-01 17:06:26');
INSERT INTO `sys_log` VALUES (627, 666, '查看用户列表', '2019-08-01 17:06:35');
INSERT INTO `sys_log` VALUES (628, 666, '查看用户列表', '2019-08-01 17:06:45');
INSERT INTO `sys_log` VALUES (629, 666, '查看用户列表', '2019-08-01 17:06:52');
INSERT INTO `sys_log` VALUES (630, 666, '查看用户列表', '2019-08-01 17:07:34');
INSERT INTO `sys_log` VALUES (631, 666, '查看用户列表', '2019-08-01 17:07:34');
INSERT INTO `sys_log` VALUES (632, 666, '查看用户列表', '2019-08-01 17:07:34');
INSERT INTO `sys_log` VALUES (633, 666, '查看用户列表', '2019-08-01 17:08:03');
INSERT INTO `sys_log` VALUES (634, 666, '查看用户列表', '2019-08-01 17:08:08');
INSERT INTO `sys_log` VALUES (635, 666, '查看用户列表', '2019-08-01 17:08:12');
INSERT INTO `sys_log` VALUES (636, 666, '查看用户列表', '2019-08-01 17:08:17');
INSERT INTO `sys_log` VALUES (637, 666, '查看用户列表', '2019-08-01 17:08:21');
INSERT INTO `sys_log` VALUES (638, 666, '查看用户列表', '2019-08-01 17:08:21');
INSERT INTO `sys_log` VALUES (639, 666, '查看用户列表', '2019-08-01 17:08:21');
INSERT INTO `sys_log` VALUES (640, 666, '查看用户列表', '2019-08-01 17:08:21');
INSERT INTO `sys_log` VALUES (641, 666, '查看用户列表', '2019-08-01 17:08:22');
INSERT INTO `sys_log` VALUES (642, 666, '查看用户列表', '2019-08-01 17:08:22');
INSERT INTO `sys_log` VALUES (643, 666, '查看用户列表', '2019-08-01 17:08:22');
INSERT INTO `sys_log` VALUES (644, 666, '查看用户列表', '2019-08-01 17:08:44');
INSERT INTO `sys_log` VALUES (645, 666, '查看用户列表', '2019-08-01 17:08:44');
INSERT INTO `sys_log` VALUES (646, 666, '查看用户列表', '2019-08-01 17:08:44');
INSERT INTO `sys_log` VALUES (647, 666, '查看用户列表', '2019-08-01 17:08:52');
INSERT INTO `sys_log` VALUES (648, 666, '查看用户列表', '2019-08-01 17:08:52');
INSERT INTO `sys_log` VALUES (649, 666, '查看用户列表', '2019-08-01 17:08:52');
INSERT INTO `sys_log` VALUES (650, 666, '查看用户列表', '2019-08-01 17:08:58');
INSERT INTO `sys_log` VALUES (651, 666, '查看用户列表', '2019-08-01 17:08:59');
INSERT INTO `sys_log` VALUES (652, 666, '查看用户列表', '2019-08-01 17:08:59');
INSERT INTO `sys_log` VALUES (653, 666, '查看用户列表', '2019-08-01 17:08:59');
INSERT INTO `sys_log` VALUES (654, 666, '查看用户列表', '2019-08-01 17:16:15');
INSERT INTO `sys_log` VALUES (655, 666, '查看用户列表', '2019-08-01 17:16:15');
INSERT INTO `sys_log` VALUES (656, 666, '查看用户列表', '2019-08-01 17:16:15');
INSERT INTO `sys_log` VALUES (657, 666, '查看用户列表', '2019-08-01 17:16:15');
INSERT INTO `sys_log` VALUES (658, 666, '查看用户列表', '2019-08-01 17:16:15');
INSERT INTO `sys_log` VALUES (659, 666, '查看用户列表', '2019-08-01 17:16:15');
INSERT INTO `sys_log` VALUES (660, 666, '查看用户列表', '2019-08-01 17:16:18');
INSERT INTO `sys_log` VALUES (661, 666, '查看用户列表', '2019-08-01 17:16:18');
INSERT INTO `sys_log` VALUES (662, 666, '查看用户列表', '2019-08-01 17:16:18');
INSERT INTO `sys_log` VALUES (663, 666, '查看用户列表', '2019-08-01 17:16:18');
INSERT INTO `sys_log` VALUES (664, 666, '查看用户列表', '2019-08-01 17:16:18');
INSERT INTO `sys_log` VALUES (665, 666, '查看用户列表', '2019-08-01 17:16:18');
INSERT INTO `sys_log` VALUES (666, 666, '查看用户列表', '2019-08-01 17:16:19');
INSERT INTO `sys_log` VALUES (667, 666, '查看用户列表', '2019-08-01 17:16:19');
INSERT INTO `sys_log` VALUES (668, 666, '查看用户列表', '2019-08-01 17:16:19');
INSERT INTO `sys_log` VALUES (669, 666, '查看用户列表', '2019-08-01 17:16:19');
INSERT INTO `sys_log` VALUES (670, 666, '查看用户列表', '2019-08-01 17:16:19');
INSERT INTO `sys_log` VALUES (671, 666, '查看用户列表', '2019-08-01 17:16:21');
INSERT INTO `sys_log` VALUES (672, 666, '查看用户列表', '2019-08-01 17:16:21');
INSERT INTO `sys_log` VALUES (673, 666, '查看用户列表', '2019-08-01 17:16:21');
INSERT INTO `sys_log` VALUES (674, 666, '查看用户列表', '2019-08-01 17:16:22');
INSERT INTO `sys_log` VALUES (675, 666, '查看用户列表', '2019-08-01 17:16:22');
INSERT INTO `sys_log` VALUES (676, 666, '查看用户列表', '2019-08-01 17:16:22');
INSERT INTO `sys_log` VALUES (677, 666, '查看用户列表', '2019-08-01 17:16:23');
INSERT INTO `sys_log` VALUES (678, 666, '查看用户列表', '2019-08-01 17:16:23');
INSERT INTO `sys_log` VALUES (679, 666, '查看用户列表', '2019-08-01 17:16:23');
INSERT INTO `sys_log` VALUES (680, 666, '查看用户列表', '2019-08-01 17:16:23');
INSERT INTO `sys_log` VALUES (681, 666, '查看用户列表', '2019-08-01 17:16:23');
INSERT INTO `sys_log` VALUES (682, 666, '查看用户列表', '2019-08-01 17:16:23');
INSERT INTO `sys_log` VALUES (683, 666, '查看用户列表', '2019-08-01 17:16:23');
INSERT INTO `sys_log` VALUES (684, 666, '查看用户列表', '2019-08-01 17:16:24');
INSERT INTO `sys_log` VALUES (685, 666, '查看用户列表', '2019-08-01 17:16:24');
INSERT INTO `sys_log` VALUES (686, 666, '查看用户列表', '2019-08-01 17:16:24');
INSERT INTO `sys_log` VALUES (687, 666, '查看用户列表', '2019-08-01 17:16:24');
INSERT INTO `sys_log` VALUES (688, 666, '查看用户列表', '2019-08-01 17:16:25');
INSERT INTO `sys_log` VALUES (689, 666, '查看用户列表', '2019-08-01 17:16:25');
INSERT INTO `sys_log` VALUES (690, 666, '查看用户列表', '2019-08-01 17:16:25');
INSERT INTO `sys_log` VALUES (691, 666, '查看用户列表', '2019-08-01 17:16:25');
INSERT INTO `sys_log` VALUES (692, 666, '查看用户列表', '2019-08-01 17:16:25');
INSERT INTO `sys_log` VALUES (693, 666, '查看用户列表', '2019-08-01 17:16:25');
INSERT INTO `sys_log` VALUES (694, 666, '查看用户列表', '2019-08-01 17:16:26');
INSERT INTO `sys_log` VALUES (695, 666, '查看用户列表', '2019-08-01 17:16:26');
INSERT INTO `sys_log` VALUES (696, 666, '查看用户列表', '2019-08-01 17:16:26');
INSERT INTO `sys_log` VALUES (697, 666, '查看用户列表', '2019-08-01 17:16:27');
INSERT INTO `sys_log` VALUES (698, 1, '查看用户列表', '2019-08-01 17:20:05');
INSERT INTO `sys_log` VALUES (699, 1, '查看用户列表', '2019-08-01 17:23:20');
INSERT INTO `sys_log` VALUES (700, 1, '查看角色列表', '2019-08-01 17:23:22');
INSERT INTO `sys_log` VALUES (701, 1, '查看系统日志', '2019-08-01 17:23:22');
INSERT INTO `sys_log` VALUES (702, 1, '查看用户列表', '2019-08-01 17:23:30');
INSERT INTO `sys_log` VALUES (703, 1, '查看功能列表', '2019-08-02 08:47:00');
INSERT INTO `sys_log` VALUES (704, 1, '查看角色树', '2019-08-02 08:47:03');
INSERT INTO `sys_log` VALUES (705, 1, '查看功能列表', '2019-08-02 08:47:03');
INSERT INTO `sys_log` VALUES (706, 1, '查看用户列表', '2019-08-02 08:47:05');
INSERT INTO `sys_log` VALUES (707, 1, '查看角色树', '2019-08-02 08:47:13');
INSERT INTO `sys_log` VALUES (708, 1, '查看角色列表', '2019-08-02 08:47:15');
INSERT INTO `sys_log` VALUES (709, 1, '查看系统日志', '2019-08-02 08:47:15');
INSERT INTO `sys_log` VALUES (710, 1, '查看功能列表', '2019-08-02 08:55:52');
INSERT INTO `sys_log` VALUES (711, 1, '查看角色树', '2019-08-02 08:55:53');
INSERT INTO `sys_log` VALUES (712, 1, '查看功能列表', '2019-08-02 08:55:53');
INSERT INTO `sys_log` VALUES (713, 1, '查看功能列表', '2019-08-02 08:55:59');
INSERT INTO `sys_log` VALUES (714, 1, '查看用户列表', '2019-08-02 08:56:00');
INSERT INTO `sys_log` VALUES (715, 1, '查看角色列表', '2019-08-02 08:56:01');
INSERT INTO `sys_log` VALUES (716, 1, '查看系统日志', '2019-08-02 08:56:01');
INSERT INTO `sys_log` VALUES (717, 1, '查看功能列表', '2019-08-02 11:11:50');
INSERT INTO `sys_log` VALUES (718, 1, '查看角色树', '2019-08-02 11:11:53');
INSERT INTO `sys_log` VALUES (719, 1, '查看功能列表', '2019-08-02 11:11:53');
INSERT INTO `sys_log` VALUES (720, 1, '查看系统日志', '2019-08-02 11:11:54');
INSERT INTO `sys_log` VALUES (721, 1, '查看角色列表', '2019-08-02 11:11:54');
INSERT INTO `sys_log` VALUES (722, 1, '查看用户列表', '2019-08-02 11:11:55');
INSERT INTO `sys_log` VALUES (723, 1, '查看用户列表', '2019-08-02 11:12:06');
INSERT INTO `sys_log` VALUES (724, 1, '查看用户列表', '2019-08-02 11:12:11');
INSERT INTO `sys_log` VALUES (725, 1, '查看用户列表', '2019-08-02 11:12:12');
INSERT INTO `sys_log` VALUES (726, 1, '查看用户列表', '2019-08-02 11:12:22');
INSERT INTO `sys_log` VALUES (727, 1, '查看用户列表', '2019-08-02 11:13:41');
INSERT INTO `sys_log` VALUES (728, 1, '系统用户退出登录', '2019-08-02 11:35:00');
INSERT INTO `sys_log` VALUES (729, 1, '查看用户列表', '2019-08-05 08:29:33');
INSERT INTO `sys_log` VALUES (730, 1, '查看用户列表', '2019-08-05 08:31:40');
INSERT INTO `sys_log` VALUES (731, 1, '查看用户列表', '2019-08-05 08:35:37');
INSERT INTO `sys_log` VALUES (732, 1, '查看用户列表', '2019-08-05 08:45:23');
INSERT INTO `sys_log` VALUES (733, 1, '查看角色树', '2019-08-05 08:45:55');
INSERT INTO `sys_log` VALUES (734, 1, '查看功能列表', '2019-08-05 08:45:56');
INSERT INTO `sys_log` VALUES (735, 1, '查看用户列表', '2019-08-05 08:45:57');
INSERT INTO `sys_log` VALUES (736, 1, '查看角色树', '2019-08-05 08:46:26');
INSERT INTO `sys_log` VALUES (737, 1, '查看功能列表', '2019-08-05 08:46:26');
INSERT INTO `sys_log` VALUES (738, 1, '查看用户列表', '2019-08-05 08:46:28');
INSERT INTO `sys_log` VALUES (739, 1, '查看用户列表', '2019-08-05 08:46:37');
INSERT INTO `sys_log` VALUES (740, 1, '查看用户列表', '2019-08-05 08:47:36');
INSERT INTO `sys_log` VALUES (741, 1, '查看用户列表', '2019-08-05 08:47:58');
INSERT INTO `sys_log` VALUES (742, 1, '查看用户列表', '2019-08-05 08:48:38');
INSERT INTO `sys_log` VALUES (743, 1, '查看用户列表', '2019-08-05 08:51:18');
INSERT INTO `sys_log` VALUES (744, 1, '查看用户列表', '2019-08-05 08:52:38');
INSERT INTO `sys_log` VALUES (745, 1, '查看功能列表', '2019-08-05 08:53:03');
INSERT INTO `sys_log` VALUES (746, 1, '查看角色树', '2019-08-05 08:53:03');
INSERT INTO `sys_log` VALUES (747, 1, '查看用户列表', '2019-08-05 08:53:05');
INSERT INTO `sys_log` VALUES (748, 1, '查看用户列表', '2019-08-05 08:53:51');
INSERT INTO `sys_log` VALUES (749, 1, '查看用户列表', '2019-08-05 09:00:02');
INSERT INTO `sys_log` VALUES (750, 1, '查看用户列表', '2019-08-05 09:11:53');
INSERT INTO `sys_log` VALUES (751, 1, '查看功能列表', '2019-08-05 10:12:20');
INSERT INTO `sys_log` VALUES (752, 1, '新增功能', '2019-08-05 10:12:54');
INSERT INTO `sys_log` VALUES (753, 1, '查看功能列表', '2019-08-05 10:12:54');
INSERT INTO `sys_log` VALUES (754, 1, '新增功能', '2019-08-05 10:13:42');
INSERT INTO `sys_log` VALUES (755, 1, '查看功能列表', '2019-08-05 10:13:42');
INSERT INTO `sys_log` VALUES (756, 1, '新增功能', '2019-08-05 10:14:13');
INSERT INTO `sys_log` VALUES (757, 1, '查看功能列表', '2019-08-05 10:14:13');
INSERT INTO `sys_log` VALUES (758, 1, '查看角色树', '2019-08-05 10:14:16');
INSERT INTO `sys_log` VALUES (759, 1, '查看功能列表', '2019-08-05 10:14:16');
INSERT INTO `sys_log` VALUES (760, 1, '新增角色功能', '2019-08-05 10:14:19');
INSERT INTO `sys_log` VALUES (761, 1, '系统用户退出登录', '2019-08-05 10:14:21');
INSERT INTO `sys_log` VALUES (762, 1, '查看功能列表', '2019-08-05 11:58:58');
INSERT INTO `sys_log` VALUES (763, 1, '查看功能列表', '2019-08-05 14:39:09');
INSERT INTO `sys_log` VALUES (764, 1, '新增功能', '2019-08-05 14:39:33');
INSERT INTO `sys_log` VALUES (765, 1, '查看功能列表', '2019-08-05 14:39:33');
INSERT INTO `sys_log` VALUES (766, 1, '查看角色树', '2019-08-05 14:39:35');
INSERT INTO `sys_log` VALUES (767, 1, '查看功能列表', '2019-08-05 14:39:35');
INSERT INTO `sys_log` VALUES (768, 1, '新增角色功能', '2019-08-05 14:39:40');
INSERT INTO `sys_log` VALUES (769, 1, '系统用户退出登录', '2019-08-05 14:39:48');
INSERT INTO `sys_log` VALUES (770, 1, '查看角色树', '2019-08-05 14:42:20');
INSERT INTO `sys_log` VALUES (771, 1, '查看功能列表', '2019-08-05 14:42:20');
INSERT INTO `sys_log` VALUES (772, 1, '查看异常ip列表', '2019-08-05 14:44:40');
INSERT INTO `sys_log` VALUES (773, 1, '查看异常ip列表', '2019-08-05 14:45:51');
INSERT INTO `sys_log` VALUES (774, 1, '查看异常ip列表', '2019-08-05 14:54:42');
INSERT INTO `sys_log` VALUES (775, 1, '查看异常ip列表', '2019-08-05 14:55:30');
INSERT INTO `sys_log` VALUES (776, 1, '查看异常ip列表', '2019-08-05 14:55:39');
INSERT INTO `sys_log` VALUES (777, 1, '查看异常ip列表', '2019-08-05 14:55:40');
INSERT INTO `sys_log` VALUES (778, 1, '查看异常ip列表', '2019-08-05 14:55:40');
INSERT INTO `sys_log` VALUES (779, 1, '查看异常ip列表', '2019-08-05 14:55:40');
INSERT INTO `sys_log` VALUES (780, 1, '查看异常ip列表', '2019-08-05 14:55:40');
INSERT INTO `sys_log` VALUES (781, 1, '查看异常ip列表', '2019-08-05 14:55:40');
INSERT INTO `sys_log` VALUES (782, 1, '查看异常ip列表', '2019-08-05 14:55:41');
INSERT INTO `sys_log` VALUES (783, 1, '查看异常ip列表', '2019-08-05 14:55:41');
INSERT INTO `sys_log` VALUES (784, 1, '查看异常ip列表', '2019-08-05 14:55:41');
INSERT INTO `sys_log` VALUES (785, 1, '查看异常ip列表', '2019-08-05 14:55:41');
INSERT INTO `sys_log` VALUES (786, 1, '查看异常ip列表', '2019-08-05 14:55:41');
INSERT INTO `sys_log` VALUES (787, 1, '查看异常ip列表', '2019-08-05 14:56:02');
INSERT INTO `sys_log` VALUES (788, 1, '查看异常ip列表', '2019-08-05 15:00:41');
INSERT INTO `sys_log` VALUES (789, 1, '查看异常ip列表', '2019-08-05 15:06:42');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '系统角色表主键，自增',
  `parent_id` bigint(11) NOT NULL DEFAULT 1 COMMENT '上级角色id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `is_stop` int(2) NOT NULL DEFAULT 0 COMMENT '是否启用;0:启用,1:禁用',
  `sort` int(2) NOT NULL DEFAULT 0 COMMENT '排序，数字越小越靠前',
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
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_function
-- ----------------------------
INSERT INTO `sys_role_function` VALUES (102, 666, 14);
INSERT INTO `sys_role_function` VALUES (103, 666, 15);
INSERT INTO `sys_role_function` VALUES (104, 666, 16);
INSERT INTO `sys_role_function` VALUES (105, 666, 1);
INSERT INTO `sys_role_function` VALUES (106, 666, 4);
INSERT INTO `sys_role_function` VALUES (206, 1, 1);
INSERT INTO `sys_role_function` VALUES (207, 1, 2);
INSERT INTO `sys_role_function` VALUES (208, 1, 5);
INSERT INTO `sys_role_function` VALUES (209, 1, 6);
INSERT INTO `sys_role_function` VALUES (210, 1, 7);
INSERT INTO `sys_role_function` VALUES (211, 1, 27);
INSERT INTO `sys_role_function` VALUES (212, 1, 3);
INSERT INTO `sys_role_function` VALUES (213, 1, 8);
INSERT INTO `sys_role_function` VALUES (214, 1, 9);
INSERT INTO `sys_role_function` VALUES (215, 1, 10);
INSERT INTO `sys_role_function` VALUES (216, 1, 11);
INSERT INTO `sys_role_function` VALUES (217, 1, 21);
INSERT INTO `sys_role_function` VALUES (218, 1, 4);
INSERT INTO `sys_role_function` VALUES (219, 1, 12);
INSERT INTO `sys_role_function` VALUES (220, 1, 13);
INSERT INTO `sys_role_function` VALUES (221, 1, 14);
INSERT INTO `sys_role_function` VALUES (222, 1, 15);
INSERT INTO `sys_role_function` VALUES (223, 1, 16);
INSERT INTO `sys_role_function` VALUES (224, 1, 22);
INSERT INTO `sys_role_function` VALUES (225, 1, 24);
INSERT INTO `sys_role_function` VALUES (226, 1, 25);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '系统用户表主键id，自增',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '登录用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '登录密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户真实姓名',
  `role_id` bigint(11) NOT NULL DEFAULT 0 COMMENT '系统角色表id',
  `allow_login` int(2) NOT NULL DEFAULT 0 COMMENT '是否允许登录;0:允许，1:禁止，2:封号中',
  `suspend_num` int(5) NOT NULL DEFAULT 0 COMMENT '账户封号次数',
  `last_active_time` datetime(0) NULL DEFAULT NULL COMMENT '用户最后活跃时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE COMMENT '登录名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1000004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'superadmin', '31b08a07bafd1363869790e5f757afc1', 'superadmin', 1, 0, 0, '2019-08-05 15:06:42', '2019-05-09 16:26:15');
INSERT INTO `sys_user` VALUES (666, 'weiziplus', 'ebe0b26e0c99fbf05e44de4e118f42d2', 'weiziplus', 666, 2, 7, '2019-08-01 17:16:24', '2019-05-10 14:30:04');
INSERT INTO `sys_user` VALUES (1000000, 'admin', '31b08a07bafd1363869790e5f757afc1', 'qqq', 0, 0, 0, '2019-05-13 09:14:32', '2019-08-07 09:38:16');
INSERT INTO `sys_user` VALUES (1000003, 'qq', 'ebe0b26e0c99fbf05e44de4e118f42d2', 'qqqqqqqqqqqqq', 666, 0, 0, '2019-06-28 17:21:07', '2019-06-28 17:18:24');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '用户表主键，自增',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '密码',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'weiziplus', 'ebe0b26e0c99fbf05e44de4e118f42d2', '2019-05-10 17:25:32');
INSERT INTO `user` VALUES (1000001, 'ceshi', 'ebe0b26e0c99fbf05e44de4e118f42d2', '2019-05-10 17:54:22');

SET FOREIGN_KEY_CHECKS = 1;
