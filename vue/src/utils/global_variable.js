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
    getAllFunctionTree: '/pc/sysFunction/getAllFunctionTree',
    /*获取功能列表*/
    getFunctionList: '/pc/sysFunction/getFunctionList',
    /*新增功能*/
    addFunction: '/pc/sysFunction/addFunction',
    /*修改功能*/
    updateFunction: '/pc/sysFunction/updateFunction',
    /*删除功能*/
    deleteFunction: '/pc/sysFunction/deleteFunction',
    /*********************角色管理*****************/
    /*获取角色树形列表*/
    getRoleTree: '/pc/sysRole/getRoleTree',
    /*获取角色所有功能列表*/
    getRoleList: '/pc/sysRole/getRoleList',
    /*获取角色的功能列表*/
    getRoleFunList: '/pc/sysRole/getRoleFunList',
    /*新增角色功能*/
    addRoleFun: '/pc/sysRole/addRoleFun',
    /*新增角色*/
    addRole: '/pc/sysRole/addRole',
    /*修改角色*/
    updateRole: '/pc/sysRole/updateRole',
    /*删除角色*/
    deleteRole: '/pc/sysRole/deleteRole',
    /*改变角色状态*/
    changeRoleIsStop: '/pc/sysRole/changeRoleIsStop',
    /*更新角色功能*/
    updateUserRole: '/pc/sysUser/updateUserRole',
    /*********************用户管理*****************/
    /*获取用户列表*/
    getUserList: '/pc/sysUser/getUserList',
    /*新增用户*/
    addUser: '/pc/sysUser/addUser',
    /*更新用户*/
    updateUser: '/pc/sysUser/updateUser',
    /*删除用户*/
    deleteUser: '/pc/sysUser/deleteUser',
    /*重置用户密码*/
    resetUserPassword: '/pc/sysUser/resetUserPassword',
    /*********************日志管理*****************/
    /*查看日志列表*/
    getLogList: '/pc/sysLog/getLogList'
};

export default {
    GLOBAL, URL
}