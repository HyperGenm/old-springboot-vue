/**
 * 全局变量
 * @type {{}}
 */
const GLOBAL = {
    //请求前缀
    base_url: process.env.VUE_APP_URL,
    //超级管理员id
    super_admin_id: 1,
    //超级管理员roleId
    super_admin_role_id: 1
};

/**
 * 请求url
 * @type {{}}
 */
const URL = {
    /*登录 */
    login: '/login',
    /*登录验证码*/
    loginValidateCode: '/getValidateCode',
    /*退出登录*/
    logout: '/logout',
    /*********************功能管理*****************/
    /*获取所有功能菜单树*/
    sysFunctionGetAllFunctionTree: '/sysFunction/getAllFunctionTree',
    /*获取所有功能菜单树*/
    sysFunctionGetAllFunctionTreeNotButton: '/sysFunction/getAllFunctionTreeNotButton',
    /*获取功能列表*/
    sysFunctionGetList: '/sysFunction/getFunctionList',
    /*新增功能*/
    sysFunctionAdd: '/sysFunction/addFunction',
    /*修改功能*/
    sysFunctionUpdate: '/sysFunction/updateFunction',
    /*删除功能*/
    sysFunctionDelete: '/sysFunction/deleteFunction',
    /*********************角色管理*****************/
    /*获取角色树形列表*/
    sysRoleGetRoleTree: '/sysRole/getRoleTree',
    /*获取角色所有功能列表*/
    sysRoleGetList: '/sysRole/getRoleList',
    /*获取角色的功能列表*/
    sysRoleGetRoleFunList: '/sysRole/getRoleFunList',
    /*新增角色功能*/
    sysRoleAddRoleFun: '/sysRole/addRoleFun',
    /*新增角色*/
    sysRoleAdd: '/sysRole/addRole',
    /*修改角色*/
    sysRoleUpdate: '/sysRole/updateRole',
    /*删除角色*/
    sysRoleDelete: '/sysRole/deleteRole',
    /*改变角色状态*/
    sysRoleChangeRoleIsStop: '/sysRole/changeRoleIsStop',
    /*更新角色功能*/
    sysRoleUpdateUserRole: '/sysUser/updateUserRole',
    /*********************用户管理*****************/
    /*获取用户列表*/
    sysUserGetUserList: '/sysUser/getUserList',
    /*新增用户*/
    sysUserAdd: '/sysUser/addUser',
    /*更新用户*/
    sysUserUpdate: '/sysUser/updateUser',
    /*删除用户*/
    sysUserDelete: '/sysUser/deleteUser',
    /*修改密码密码*/
    sysUserUpdateUserPassword: '/sysUser/updatePassword',
    /*重置用户密码*/
    sysUserResetUserPassword: '/sysUser/resetUserPassword',
    /*解除封号*/
    sysUserRelieveSuspend: '/sysUser/relieveSuspend',
    /*********************日志管理*****************/
    /*查看日志列表*/
    sysLogGetLogList: '/sysLog/getLogList'
};

export default {
    GLOBAL, URL
}