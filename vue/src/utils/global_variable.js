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
    super_admin_role_id: 1,
    //请求头token名称
    token: 'token',
    //后台状态码
    axios_result_code: {
        //成功
        success: 200,
        //token异常
        errorToken: 401,
        //error
        error: 402,
        //没有权限,拒绝访问
        errorRole: 403,
        //后台系统异常
        errorException: 500
    }
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
    /*************系统管理*****************/
    system: {
        /*********************功能管理*****************/
        sysFunction: {
            /*获取所有功能树形数据*/
            getAllFunctionTreePageList: '/sysFunction/getAllFunctionTreePageList',
            /*获取所有功能菜单树*/
            getAllFunctionTree: '/sysFunction/getAllFunctionTree',
            /*获取所有功能菜单树*/
            getAllFunctionTreeNotButton: '/sysFunction/getAllFunctionTreeNotButton',
            /*新增功能*/
            add: '/sysFunction/addFunction',
            /*修改功能*/
            update: '/sysFunction/updateFunction',
            /*删除功能*/
            delete: '/sysFunction/deleteFunction',
        },
        /*********************角色管理*****************/
        sysRole: {
            /*获取所有角色树形结构*/
            getAllRoleTreePageList: '/sysRole/getAllRoleTreePageList',
            /*获取角色树形列表*/
            getRoleTree: '/sysRole/getRoleTree',
            /*获取角色所有功能列表*/
            getList: '/sysRole/getRoleList',
            /*获取角色的功能列表*/
            getRoleFunList: '/sysFunction/getRoleFunList',
            /*新增角色功能*/
            addRoleFun: '/sysRole/addRoleFun',
            /*新增角色*/
            add: '/sysRole/addRole',
            /*修改角色*/
            update: '/sysRole/updateRole',
            /*删除角色*/
            delete: '/sysRole/deleteRole',
            /*改变角色状态*/
            changeRoleIsStop: '/sysRole/changeRoleIsStop',
            /*更新角色功能*/
            updateUserRole: '/sysUser/updateUserRole',
        },
        /*********************用户管理*****************/
        sysUser: {
            /*获取用户列表*/
            getPageList: '/sysUser/getPageList',
            /*新增用户*/
            add: '/sysUser/addUser',
            /*更新用户*/
            update: '/sysUser/updateUser',
            /*删除用户*/
            delete: '/sysUser/deleteUser',
            /*修改密码密码*/
            updateUserPassword: '/sysUser/updatePassword',
            /*重置用户密码*/
            resetUserPassword: '/sysUser/resetUserPassword',
            /*解除封号*/
            relieveSuspend: '/sysUser/relieveSuspend',
            /*修改头像*/
            updateIcon: '/sysUser/updateIcon'
        },
        /*********************系统用户日志管理*****************/
        sysUserLog: {
            /*查看日志列表*/
            getPageList: '/sysUserLog/getPageList',
        },
        /*********************用户日志管理*****************/
        userLog: {
            /*查看日志列表*/
            getPageList: '/userLog/getPageList',
        },
    },
    /********字典管理*****************/
    dataDictionary: {
        /*********************ip管理*****************/
        ipManager: {
            /*获取ip规则*/
            getIpRole: '/dataDictionary/ipManager/getIpRole',
            /*更新ip规则*/
            updateIpRole: '/dataDictionary/ipManager/updateIpRole',
            /*获取ip名单*/
            getIpList: '/dataDictionary/ipManager/getIpList',
            /*删除ip*/
            deleteIp: '/dataDictionary/ipManager/deleteIp',
            /*新增ip*/
            addIp: '/dataDictionary/ipManager/addIp'
        }
    },
    /********常用工具*****************/
    tools: {
        /*文件上传*/
        upload: '/tools/upload',
        /*文件上传*/
        downTemp: '/tools/downTemp'
    }
};

export default {
    GLOBAL, URL
}