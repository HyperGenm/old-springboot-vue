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
    },
    //放到window中的变量key
    window: {
        axiosCancelToken: '_axiosCancelToken'
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
            /*获取所有功能菜单树*/
            getAllFunctionTree: '/sysFunction/getAllFunctionTree',
            /*获取所有功能菜单树*/
            getAllFunctionTreeNotButton: '/sysFunction/getAllFunctionTreeNotButton',
            /*获取功能列表*/
            getList: '/sysFunction/getFunctionList',
            /*新增功能*/
            add: '/sysFunction/addFunction',
            /*修改功能*/
            update: '/sysFunction/updateFunction',
            /*删除功能*/
            delete: '/sysFunction/deleteFunction',
        },
        /*********************角色管理*****************/
        sysRole: {
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
        },
        /*********************日志管理*****************/
        sysLog: {
            /*查看日志列表*/
            getPageList: '/sysLog/getPageList',
        }
    },
    /********字典管理*****************/
    dataDictionary: {
        /*********************ip名单*****************/
        ipFilter: {
            /*分页数据*/
            getPageList: '/dataDictionary/ipFilter/getPageList',
            /*新增ip*/
            add: '/dataDictionary/ipFilter/add',
            /*删除ip*/
            delete: '/dataDictionary/ipFilter/delete'
        },
        /*********************异常ip管理*****************/
        abnormalIp: {
            /*查看分页数据*/
            getPageList: '/dataDictionary/abnormalIp/getPageList',
        }
    },
    /********常用工具*****************/
    tools: {
        /*文件上传*/
        upload: '/tools/upload'
    }
};

export default {
    GLOBAL, URL
}