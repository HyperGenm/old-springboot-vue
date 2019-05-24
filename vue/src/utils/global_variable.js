/**
 * 全局变量
 * @type {{}}
 */
const GLOBAL = {
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
    /*获取所有功能菜单树*/
    getAllFunctionTree: '/pc/getAllFunctionTree',
    /*获取功能列表*/
    getFunctionList: '/pc/getFunctionList',
    /*新增功能*/
    addFunction: '/pc/addFunction',
    /*修改功能*/
    updateFunction: '/pc/updateFunction',
    /*删除功能*/
    deleteFunction: '/pc/deleteFunction',
    /*获取角色树形列表*/
    getRoleTree: '/pc/getRoleTree',
    /*获取角色所有功能列表*/
    getRoleList: '/pc/getRoleList',
    /*获取角色的功能列表*/
    getRoleFunList: '/pc/getRoleFunList',
    /*新增角色功能*/
    addRoleFun: '/pc/addRoleFun',
    /*更新角色功能*/
    updateUserRole: '/pc/updateUserRole',
    /*新增角色*/
    addRole: '/pc/addRole',
    /*修改角色*/
    updateRole: '/pc/updateRole',
    /*删除角色*/
    deleteRole: '/pc/deleteRole',
    /*改变角色状态*/
    changeRoleIsStop: '/pc/changeRoleIsStop',
    /*获取用户列表*/
    getUserList: '/pc/getUserList',
    /*新增用户*/
    addUser: '/pc/addUser',
    /*更新用户*/
    updateUser: '/pc/updateUser',
    /*删除用户*/
    deleteUser: '/pc/deleteUser',
    /*新增用户角色*/
    addUserRole: '/pc/addUserRole',
    /*重置用户密码*/
    resetUserPassword: '/pc/resetUserPassword',
    /*查看日志列表*/
    getLogList: '/pc/getLogList'
};

export default {
    GLOBAL, URL
}