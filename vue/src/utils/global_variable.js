/**
 * 全局变量
 * @type {{}}
 */
const GLOBAL = {
    //请求前缀
    base_url: process.env.VUE_APP_URL
};

/**
 * 请求url
 * @type {{}}
 */
const URL = {
    /*登录 */
    login: '/pc/login',
    /*登录验证码*/
    loginValidateCode: '/pc/getValidateCode',
    /*退出登录*/
    logout: '/pc/logout',
    /*********************功能管理*****************/
    /*获取所有功能菜单树*/
    sysFunctionGetAllFunctionTree: '/pc/sysFunction/getAllFunctionTree',
    /*获取所有功能菜单树*/
    sysFunctionGetAllFunctionTreeNotButton: '/pc/sysFunction/getAllFunctionTreeNotButton',
    /*获取功能列表*/
    sysFunctionGetList: '/pc/sysFunction/getFunctionList',
    /*新增功能*/
    sysFunctionAdd: '/pc/sysFunction/addFunction',
    /*修改功能*/
    sysFunctionUpdate: '/pc/sysFunction/updateFunction',
    /*删除功能*/
    sysFunctionDelete: '/pc/sysFunction/deleteFunction',
    /*********************角色管理*****************/
    /*获取角色树形列表*/
    sysRoleGetRoleTree: '/pc/sysRole/getRoleTree',
    /*获取角色所有功能列表*/
    sysRoleGetList: '/pc/sysRole/getRoleList',
    /*获取角色的功能列表*/
    sysRoleGetRoleFunList: '/pc/sysRole/getRoleFunList',
    /*新增角色功能*/
    sysRoleAddRoleFun: '/pc/sysRole/addRoleFun',
    /*新增角色*/
    sysRoleAdd: '/pc/sysRole/addRole',
    /*修改角色*/
    sysRoleUpdate: '/pc/sysRole/updateRole',
    /*删除角色*/
    sysRoleDelete: '/pc/sysRole/deleteRole',
    /*改变角色状态*/
    sysRoleChangeRoleIsStop: '/pc/sysRole/changeRoleIsStop',
    /*更新角色功能*/
    sysRoleUpdateUserRole: '/pc/sysUser/updateUserRole',
    /*********************用户管理*****************/
    /*获取用户列表*/
    sysUserGetUserList: '/pc/sysUser/getUserList',
    /*新增用户*/
    sysUserAdd: '/pc/sysUser/addUser',
    /*更新用户*/
    sysUserUpdate: '/pc/sysUser/updateUser',
    /*删除用户*/
    sysUserDelete: '/pc/sysUser/deleteUser',
    /*修改密码密码*/
    sysUserUpdateUserPassword: '/pc/sysUser/updatePassword',
    /*重置用户密码*/
    sysUserResetUserPassword: '/pc/sysUser/resetUserPassword',
    /*********************日志管理*****************/
    /*查看日志列表*/
    sysLogGetLogList: '/pc/sysLog/getLogList'
};

export default {
    GLOBAL, URL
}