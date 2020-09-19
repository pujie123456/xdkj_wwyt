//package com.jib.wwyt.utils;
//
//import com.jib.wwyt.mapper.TCompUserDepMapper;
//import com.jib.wwyt.model.BisEnterprise;
//import com.jib.wwyt.model.Permission;
//import com.jib.wwyt.model.User;
//import com.jib.wwyt.service.*;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.session.Session;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class UserUtils {
//
//
//    @Autowired
//    private BisEnterpriseService bisEnterpriseServiceImpl;
//    @Autowired
//    private UserService userServiceImpl;
//    @Autowired
//    private DepartmentService departmentService;
//    @Autowired
//    private TCompUserDepMapper tCompUserDepMapper;
//    @Autowired
//    private PermissionService permissionServiceImpl;
//    @Autowired
//    private SystemService systemService;
//
//    /**
//     * 从session中取出User
//     * @return
//     */
//    public User getUser() {
//        Session session = SecurityUtils.getSubject().getSession();
//        User user = (User) session.getAttribute(StateInvariant.SESSION_USER);
//        return user;
//    }
//
//    /**
//     * 获取权限分析获取map对象
//     * @param
//     * @return Map对象
//     */
//    public Map getAuthorityMap(User user) {
//        Map<String,Object> authoritymap= new HashMap<>();
//        //安监
//        if("0".equals(user.getUsertype())){
//            authoritymap.put("xzqy", user.getXzqy());
//            if(user.getUserroleflg()!=null&&user.getUserroleflg()!=0){
//                authoritymap.put("jglx",user.getUserroleflg());
//            }
//        }else if("5".equals(user.getUsertype())){//第三方
//            authoritymap.put("cjz", user.getId());
//        }else if("1".equals(user.getUsertype())){//企业
//            BisEnterprise be = this.bisEnterpriseServiceImpl.findInfoById(user.getId2());
//            if(be.getIsbloc()!=null&&be.getIsbloc()==1)//判断是否为集团公司
//                authoritymap.put("fid", user.getId2());
//            else
//                authoritymap.put("qyid", user.getId2());
//        }
//
//        //判断用户部门权限
//        Map dep=user.getDepartment();
//        if(dep!=null){
//            String qxbs=(String)dep.get("m4");
//            if(qxbs.equals("1"))//本级
//                authoritymap.put("depcode1", dep.get("code"));
//            if(qxbs.equals("2"))//下级
//                authoritymap.put("depcode2", dep.get("code"));
//        }
//
//        //判断用户部门范围
//        String depids="";
//        if(user.getDepIdList()!=null) {
//            List<Long> depIdList = user.getDepIdList();
//            if (depIdList.size() > 0) {
//                for (int i = 0; i <= depIdList.size() - 1; i++) {
//                    depids += depIdList.get(i) + ",";
//                }
//                depids = depids.substring(0,depids.length() - 1);
//            }
//            String depid= dep.get("id")+"";
//            System.out.println("部门id="+depid);
//            if(!depids.equals(depid))
//                authoritymap.put("depids", depids);
//        }
//        return authoritymap;
//    }
//
//
//
//
//    /**
//     * 根据userId获取用户
//     * @param
//     * @return Map对象
//     */
//    public User getUserByID(String userId) {
//        //
//        User user = this.userServiceImpl.getUserById(userId);
//        Map param2 = new HashMap<>();
//        param2.put("id", user.getDepartmen());
//        Map map2 = this.departmentService.getInforById(param2);
//        List<Long> depIdList=this.tCompUserDepMapper.getCompDepIdList(user.getId());
//        user.setDepartment(map2);
//        user.setDepIdList(depIdList);
//        return user;
//    }
//
//    /**
//     * 根据userId获取用户    不需要部门和部门权限
//     * @param
//     * @return
//     */
//    public  User getUserByID2(String userId) {
//        User user = this.userServiceImpl.getUserById(userId);
//        return user;
//    }
//
//
//
//    /**
//     * 根据userId获取用户    包含权限
//     * @param
//     * @return
//     */
//    public  User getUserByID3(String userId) {
//        User user = this.userServiceImpl.getUserById(userId);
//        //赋予权限
//        List<Permission> permissions = permissionServiceImpl.getPermissions(user.getId());
//        user.setPermissions(permissions);
//        return user;
//    }
//
//    /**
//     * 根据userId获取用户    包含菜单权限  按钮权限
//     * @param
//     * @return
//     */
//    public  User getUserByID4(String userId) {
//        User user = this.userServiceImpl.getUserById(userId);
//        //赋予权限
//        systemService.setUserPermissions(user);//初始化权限
//        return user;
//    }
//}
